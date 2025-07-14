package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.models.Quarto;

import com.bd.ufrn.projeto.dtos.ItemPedido;
import com.bd.ufrn.projeto.interfaces.StrongEntity;

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

        public List<Pedido> findByNumeroQuarto(int numeroQuarto) {
        String sql = """ 
        SELECT p.id_pedido, p.data_pedido, p.data_entrega, p.numero_quarto, q.numero as numero_quarto, 
            php.preco_item, php.quantidade_item, pr.id_produto, pr.nome, pr.preco_atual, pr.quantidade
        FROM pedido p
        INNER JOIN quarto q ON p.numero_quarto = q.numero
        INNER JOIN pedido_has_produto php ON p.id_pedido = php.id_pedido
        INNER JOIN produto pr ON php.id_produto = pr.id_produto
                WHERE p.numero_quarto = ?
        """;
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, numeroQuarto);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Map<Integer, Pedido> pedidoMap = new LinkedHashMap<>();
                while (resultSet.next()) {
                    int pedidoId = resultSet.getInt("id_pedido");
                    Pedido pedido = pedidoMap.computeIfAbsent(pedidoId, id -> {
                        try {
                            Timestamp dataPedidoTs = resultSet.getTimestamp("data_pedido");
                            Timestamp dataEntregaTs = resultSet.getTimestamp("data_entrega");

                            Quarto quarto = Quarto.builder()
                                    .numero(resultSet.getInt("numero_quarto"))
                                    .build();

                            return Pedido.builder()
                                    .id(pedidoId)
                                    .dataPedido(dataPedidoTs != null ? dataPedidoTs.toLocalDateTime() : null)
                                    .dataEntrega(dataEntregaTs != null ? dataEntregaTs.toLocalDateTime() : null)
                                    .quarto(quarto)
                                    .itemPedidos(new ArrayList<>())
                                    .build();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    ItemPedido item = ItemPedido.builder()
                            .produto(Produto.builder()
                                    .id(resultSet.getInt("id_produto"))
                                    .nome(resultSet.getString("nome"))
                                    .precoAtual(resultSet.getDouble("preco_atual"))
                                    .quantidade(resultSet.getInt("quantidade"))
                                    .build())
                            .preco(resultSet.getDouble("preco_item"))
                            .quantidade(resultSet.getInt("quantidade_item"))
                            .build();

                    pedido.getItemPedidos().add(item);
                }
                pedidos.addAll(pedidoMap.values());
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
    SELECT p.id_pedido, p.data_pedido, p.data_entrega, p.numero_quarto, q.numero as numero_quarto, 
        php.preco_item, php.quantidade_item, pr.id_produto, pr.nome, pr.preco_atual, pr.quantidade
    FROM pedido p
    INNER JOIN quarto q ON p.numero_quarto = q.numero
    INNER JOIN pedido_has_produto php ON p.id_pedido = php.id_pedido
    INNER JOIN produto pr ON php.id_produto = pr.id_produto
    WHERE p.id_pedido = ?
    """;

        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToPedido(resultSet);
                }
                return null; // Or throw an exception if a Pedido should always be found
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

        Quarto quarto = Quarto.builder()
                .numero(resultSet.getInt("numero_quarto"))
                .build();

        Pedido pedido = Pedido.builder()
                .id(resultSet.getInt("id_pedido"))
                .dataPedido(dataPedidoTs != null ? dataPedidoTs.toLocalDateTime() : null)
                .dataEntrega(dataEntregaTs != null ? dataEntregaTs.toLocalDateTime() : null)
                .quarto(quarto)
                .itemPedidos(new ArrayList<>()) // Init empty list
                .build();

        // First row was read, process items
        do {
            ItemPedido item = ItemPedido.builder()
                    .produto(Produto.builder()
                            .id(resultSet.getInt("id_produto"))
                            .nome(resultSet.getString("nome"))
                            .precoAtual(resultSet.getDouble("preco_atual"))
                            .quantidade(resultSet.getInt("quantidade"))
                            .build())
                    .preco(resultSet.getDouble("preco_item"))
                    .quantidade(resultSet.getInt("quantidade_item"))
                    .build();

            pedido.getItemPedidos().add(item);
        } while (resultSet.next());

        return pedido;
    }

    @Override
    public void save(Pedido pedido) {
        boolean isInsert = (pedido.getId() == null);
        String insertPedidoSql = "INSERT INTO pedido (data_pedido, data_entrega, numero_quarto) VALUES (?, ?, ?)";
        String updatePedidoSql = "UPDATE pedido SET data_pedido = ?, data_entrega = ?, numero_quarto = ? WHERE id_pedido = ?";
        String deleteItemsSql = "DELETE FROM pedido_has_produto WHERE id_pedido = ?";
        String insertItemSql = """
        INSERT INTO pedido_has_produto (id_pedido, id_produto, preco_item, quantidade_item)
        VALUES (?, ?, ?, ?)
        """;
        Connection connection = null;

        try {
            connection = connectionFactory.connection();
            connection.setAutoCommit(false);

            if (isInsert) {
                try (PreparedStatement stmt = connection.prepareStatement(insertPedidoSql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setTimestamp(1, toTimestamp(pedido.getDataPedido()));
                    stmt.setTimestamp(2, toTimestamp(pedido.getDataEntrega()));
                    stmt.setInt(3, pedido.getQuarto().getNumero());
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
                    stmt.setInt(3, pedido.getQuarto().getNumero());
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
                for (ItemPedido item : pedido.getItemPedidos()) {
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
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            throw new RuntimeException("Failed to save Pedido", e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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
               q.numero as numero_quarto, 
               php.preco_item, php.quantidade_item,
               pr.id_produto, pr.nome, pr.preco_atual, pr.quantidade
        FROM pedido p
        INNER JOIN quarto q ON p.numero_quarto = q.numero
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
                    // Create Quarto
                    Quarto quarto = Quarto.builder()
                            .numero(rs.getInt("numero_quarto"))
                            .build();

                    // Create Pedido
                    pedido = Pedido.builder()
                            .id(pedidoId)
                            .dataPedido(rs.getTimestamp("data_pedido") != null ?
                                    rs.getTimestamp("data_pedido").toLocalDateTime() : null)
                            .dataEntrega(rs.getTimestamp("data_entrega") != null ?
                                    rs.getTimestamp("data_entrega").toLocalDateTime() : null)
                            .quarto(quarto)
                            .itemPedidos(new ArrayList<>())
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

                    ItemPedido item = ItemPedido.builder()
                            .produto(produto)
                            .preco(rs.getDouble("preco_item"))
                            .quantidade(rs.getInt("quantidade_item"))
                            .build();

                    pedido.getItemPedidos().add(item);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch pedidos", e);
        }

        return new ArrayList<>(pedidoMap.values());
    }
}
