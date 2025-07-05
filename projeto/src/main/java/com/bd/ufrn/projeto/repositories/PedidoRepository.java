package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.interfaces.StrongEntity;
import com.bd.ufrn.projeto.models.Pedido;
import com.bd.ufrn.projeto.models.Produto;
import com.bd.ufrn.projeto.services.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class PedidoRepository extends AbstractRepository<Pedido> implements StrongEntity<Pedido, Integer> {

    private final ConnectionFactory connectionFactory;

    @Autowired
    public PedidoRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public List<Pedido> findByCpf(Integer cpf) {
        String sql = "SELECT id_pedido, data_pedido, data_entega, cpf FROM pedido WHERE cpf = ?";
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, cpf);
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
        String sql = "SELECT id_pedido, data_pedido, data_entega, cpf FROM pedido WHERE id_pedido = ?";
        Pedido pedido = null;

        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    pedido = mapResultSetToPedido(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedido;
    }

    private Pedido mapResultSetToPedido(ResultSet resultSet) throws SQLException {
        Timestamp dataPedidoTs = resultSet.getTimestamp("data_pedido");
        Timestamp dataEntregaTs = resultSet.getTimestamp("data_entega");

        return Pedido.builder()
                .id(resultSet.getInt("id_pedido"))
                .dataPedido(dataPedidoTs != null ? dataPedidoTs.toLocalDateTime() : null)
                .dataEntrega(dataEntregaTs != null ? dataEntregaTs.toLocalDateTime() : null)
                .cpfHospede(resultSet.getInt("cpf"))
                .build();
    }

    @Override
    public void save(Pedido pedido) {
        String sql = "INSERT INTO pedido (id_pedido, data_pedido, data_entega, cpf) VALUES (?, ?, ?, ?)";

        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, pedido.getId());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(pedido.getDataPedido()));

            if (pedido.getDataEntrega() != null) {
                preparedStatement.setTimestamp(3, Timestamp.valueOf(pedido.getDataEntrega()));
            } else {
                preparedStatement.setNull(3, Types.TIMESTAMP);
            }

            preparedStatement.setInt(4, pedido.getCpfHospede());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Pedido pedido) {
        String sql = "DELETE FROM pedido WHERE id_pedido = ?";

        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, pedido.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Pedido> findAll() {
        String sql = "SELECT id_pedido, data_pedido, data_entega, cpf FROM pedido";
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                pedidos.add(mapResultSetToPedido(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidos;
    }

    public List<Produto> findProdutosPedido(Integer idPedido) {
        String sql = """
        SELECT p.id_produto, p.preco_atual, p.quantidade
        FROM produto p
        INNER JOIN pedido_has_produto php ON p.id_produto = php.id_produto
        WHERE php.id_pedido = ?
        """;

        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idPedido);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Produto> produtos = new ArrayList<>();
                while (resultSet.next()) {
                    Produto produto = Produto.builder()
                            .id(resultSet.getInt("id_produto"))
                            .precoAtual(resultSet.getDouble("preco_atual"))
                            .quantidade(resultSet.getInt("quantidade"))
                            .build();
                    produtos.add(produto);
                }
                return produtos;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
