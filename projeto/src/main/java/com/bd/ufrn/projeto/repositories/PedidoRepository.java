package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.dtos.ItemPedidoDTO;
import com.bd.ufrn.projeto.interfaces.StrongEntity;
import com.bd.ufrn.projeto.models.Hospede;
import com.bd.ufrn.projeto.models.Pedido;
import com.bd.ufrn.projeto.models.Produto;
import com.bd.ufrn.projeto.services.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PedidoRepository extends AbstractRepository<Pedido> implements StrongEntity<Pedido, Integer> {

    private final ConnectionFactory connectionFactory;

    @Autowired
    public PedidoRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public List<Pedido> findByCpf(String cpf) {
        String sql = """ 
        SELECT p.id_pedido, p.data_pedido, p.data_entrega, p.cpf, h.data_nasc, h.nome_sobrenome,
            php.preco_item, php.quantidade_item, pr.id_produto, pr.nome, pr.preco_atual, pr.quantidade
        FROM pedido p
        INNER JOIN hospede h ON p.cpf = h.cpf
        INNER JOIN pedido_has_produto php ON p.id_pedido = php.id_pedido
        INNER JOIN produto pr ON php.id_produto = pr.id_produto
        WHERE p.cpf = ?
        """;
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, cpf);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    pedidos.add(mapResultSetToPedido(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        return pedidos;
    }

    @Override
    public Pedido findById(Integer id) {
        String sql = """
    SELECT p.id_pedido, p.data_pedido, p.data_entrega, p.cpf, h.data_nasc, h.nome_sobrenome,
        php.preco_item, php.quantidade_item, pr.id_produto, pr.nome, pr.preco_atual, pr.quantidade
    FROM pedido p
    INNER JOIN hospede h ON p.cpf = h.cpf
    INNER JOIN pedido_has_produto php ON p.id_pedido = php.id_pedido
    INNER JOIN produto pr ON php.id_produto = pr.id_produto
    WHERE p.id_pedido = ?
    """;

        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return mapResultSetToPedido(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    private Pedido mapResultSetToPedido(ResultSet resultSet) throws SQLException {
        // Read first row
        Timestamp dataPedidoTs = resultSet.getTimestamp("data_pedido");
        Timestamp dataEntregaTs = resultSet.getTimestamp("data_entrega");
        Timestamp dataNascTs = resultSet.getTimestamp("data_nasc");

        Hospede hospede = Hospede.builder()
                .cpf(resultSet.getString("cpf"))
                .nome(resultSet.getString("nome_sobrenome"))
                .dataNascimento(dataNascTs != null ? dataNascTs.toLocalDateTime() : null)
                .build();

        Pedido pedido = Pedido.builder()
                .id(resultSet.getInt("id_pedido"))
                .dataPedido(dataPedidoTs != null ? dataPedidoTs.toLocalDateTime() : null)
                .dataEntrega(dataEntregaTs != null ? dataEntregaTs.toLocalDateTime() : null)
                .hospede(hospede)
                .itemPedidoDTOS(new ArrayList<>()) // Init empty list
                .build();

        // First row was read, process items
        do {
            ItemPedidoDTO item = ItemPedidoDTO.builder()
                    .produto(Produto.builder()
                            .id(resultSet.getInt("id_produto"))
                            .nome(resultSet.getString("nome"))
                            .precoAtual(resultSet.getDouble("preco_atual"))
                            .quantidade(resultSet.getInt("quantidade"))
                            .build())
                    .preco(resultSet.getDouble("preco_item"))
                    .quantidade(resultSet.getInt("quantidade_item"))
                    .build();

            pedido.getItemPedidoDTOS().add(item);
        } while (resultSet.next());

        return pedido;
    }

    @Override
    public void save(Pedido pedido) {
        boolean isInsert = (pedido.getId() == null);
        String insertPedidoSql = """
        INSERT INTO pedido (data_pedido, data_entrega, cpf)
        VALUES (?, ?, ?)
        """;
        String updatePedidoSql = """
        UPDATE pedido SET data_pedido = ?, data_entrega = ?, cpf = ?
        WHERE id_pedido = ?
        """;
        String deleteItemsSql = "DELETE FROM pedido_has_produto WHERE id_pedido = ?";
        String insertItemSql = """
        INSERT INTO pedido_has_produto (id_pedido, id_produto, preco_item, quantidade_item)
        VALUES (?, ?, ?, ?)
        """;

        try (Connection connection = connectionFactory.connection()) {
            connection.setAutoCommit(false);

            if (isInsert) {
                try (PreparedStatement stmt = connection.prepareStatement(insertPedidoSql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setTimestamp(1, toTimestamp(pedido.getDataPedido()));
                    stmt.setTimestamp(2, toTimestamp(pedido.getDataEntrega()));
                    stmt.setString(3, pedido.getHospede().getCpf());
                    stmt.executeUpdate();

                    try (ResultSet keys = stmt.getGeneratedKeys()) {
                        if (keys.next()) {
                            pedido.setId(keys.getInt(1));
                        } else {
                            throw new SQLException("Failed to retrieve generated id for pedido.");
                        }
                    }
                }
            } else {
                // Update pedido table
                try (PreparedStatement stmt = connection.prepareStatement(updatePedidoSql)) {
                    stmt.setTimestamp(1, toTimestamp(pedido.getDataPedido()));
                    stmt.setTimestamp(2, toTimestamp(pedido.getDataEntrega()));
                    stmt.setString(3, pedido.getHospede().getCpf());
                    stmt.setInt(4, pedido.getId());
                    stmt.executeUpdate();
                }

                // Clean up existing items
                try (PreparedStatement stmt = connection.prepareStatement(deleteItemsSql)) {
                    stmt.setInt(1, pedido.getId());
                    stmt.executeUpdate();
                }
            }

            // Insert all items
            try (PreparedStatement stmt = connection.prepareStatement(insertItemSql)) {
                for (ItemPedidoDTO item : pedido.getItemPedidoDTOS()) {
                    stmt.setInt(1, pedido.getId());
                    stmt.setInt(2, item.getProduto().getId());
                    stmt.setDouble(3, item.getPreco());
                    stmt.setInt(4, item.getQuantidade());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save Pedido", e);
        }
    }

    private Timestamp toTimestamp(LocalDateTime ldt) {
        return (ldt != null) ? Timestamp.valueOf(ldt) : null;
    }

    @Override
    public void delete(Pedido pedido) {
        if (pedido == null || pedido.getId() == null) {
            throw new IllegalArgumentException("Pedido or its ID must not be null.");
        }
        String sql = "DELETE FROM pedido WHERE id_pedido = ?";

        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, pedido.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete Pedido", e);
        }
    }

    @Override
    public List<Pedido> findAll() {
        String sql = """
        SELECT p.id_pedido, p.data_pedido, p.data_entrega,
               h.cpf, h.data_nasc, h.nome_sobrenome,
               php.preco_item, php.quantidade_item,
               pr.id_produto, pr.nome, pr.preco_atual, pr.quantidade
        FROM pedido p
        INNER JOIN hospede h ON p.cpf = h.cpf
        LEFT JOIN pedido_has_produto php ON p.id_pedido = php.id_pedido
        LEFT JOIN produto pr ON php.id_produto = pr.id_produto
        ORDER BY p.id_pedido
        """;

        Map<Integer, Pedido> pedidoMap = new LinkedHashMap<>();

        try (Connection connection = connectionFactory.connection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int pedidoId = rs.getInt("id_pedido");

                Pedido pedido = pedidoMap.get(pedidoId);
                if (pedido == null) {
                    // Create Hospede
                    Hospede hospede = Hospede.builder()
                            .cpf(rs.getString("cpf"))
                            .nome(rs.getString("nome_sobrenome"))
                            .dataNascimento(rs.getTimestamp("data_nasc") != null ?
                                    rs.getTimestamp("data_nasc").toLocalDateTime() : null)
                            .build();

                    // Create Pedido
                    pedido = Pedido.builder()
                            .id(pedidoId)
                            .dataPedido(rs.getTimestamp("data_pedido") != null ?
                                    rs.getTimestamp("data_pedido").toLocalDateTime() : null)
                            .dataEntrega(rs.getTimestamp("data_entrega") != null ?
                                    rs.getTimestamp("data_entrega").toLocalDateTime() : null)
                            .hospede(hospede)
                            .itemPedidoDTOS(new ArrayList<>())
                            .build();

                    pedidoMap.put(pedidoId, pedido);
                }

                int produtoId = rs.getInt("id_produto");
                if (produtoId != 0) {
                    Produto produto = Produto.builder()
                            .id(produtoId)
                            .nome(rs.getString("nome"))
                            .precoAtual(rs.getDouble("preco_atual"))
                            .quantidade(rs.getInt("quantidade"))
                            .build();

                    ItemPedidoDTO item = ItemPedidoDTO.builder()
                            .produto(produto)
                            .preco(rs.getDouble("preco_item"))
                            .quantidade(rs.getInt("quantidade_item"))
                            .build();

                    pedido.getItemPedidoDTOS().add(item);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch pedidos", e);
        }

        return new ArrayList<>(pedidoMap.values());
    }
}
