package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.models.PedidoHasProduto;
import com.bd.ufrn.projeto.services.ConnectionFactory;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PedidoHasProdutoRepository extends AbstractRepository<PedidoHasProduto> {
    private final ConnectionFactory connectionFactory;

    @Autowired
    public PedidoHasProdutoRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public PedidoHasProduto findByPedidoIdAndProdutoId(Integer idPedido, Integer idProduto) {
        PedidoHasProduto pedidoHasProduto = null;
        String sql = "SELECT id_pedido, id_produto, preco_item FROM pedido_has_produto WHERE id_pedido = ? AND id_produto = ?";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPedido);
            preparedStatement.setInt(2, idProduto);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    pedidoHasProduto = mapResultSetToPedidoHasProduto(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidoHasProduto;
    }

    private PedidoHasProduto mapResultSetToPedidoHasProduto(ResultSet resultSet) throws SQLException {
        return PedidoHasProduto.builder()
                .idPedido(resultSet.getInt("id_pedido"))
                .idProduto(resultSet.getInt("id_produto"))
                .precoItem(resultSet.getDouble("preco_item"))
                .build();
    }

    @Override
    public void save(PedidoHasProduto pedidoHasProduto) {
        Connection connection = null;
        try {
            connection = connectionFactory.connection();
            connection.setAutoCommit(false);

            String sql = "INSERT INTO pedido_has_produto (id_pedido, id_produto, preco_item) VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE preco_item = VALUES(preco_item)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, pedidoHasProduto.getIdPedido());
                preparedStatement.setInt(2, pedidoHasProduto.getIdProduto());
                preparedStatement.setDouble(3, pedidoHasProduto.getPrecoItem());
                preparedStatement.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
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


    public void deleteByPedidoIdAndProdutoId(Integer idPedido, Integer idProduto) {
        Connection connection = null;
        try {
            connection = connectionFactory.connection();
            connection.setAutoCommit(false);

            String sql = "DELETE FROM pedido_has_produto WHERE id_pedido = ? AND id_produto = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, idPedido);
                preparedStatement.setInt(2, idProduto);
                preparedStatement.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
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

    @Override
    public void delete(PedidoHasProduto entity) {
        if (entity.getIdPedido() == null || entity.getIdProduto() == null) {
            throw new IllegalArgumentException("Both idPedido and idProduto must be provided to delete a PedidoHasProduto entry.");
        }
        deleteByPedidoIdAndProdutoId(entity.getIdPedido(), entity.getIdProduto());
    }

    @Override
    public List<PedidoHasProduto> findAll() {
        List<PedidoHasProduto> list = new ArrayList<>();
        String sql = "SELECT id_pedido, id_produto, preco_item FROM pedido_has_produto";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    list.add(mapResultSetToPedidoHasProduto(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

     public List<PedidoHasProduto> findByPedidoId(Integer idPedido) {
        List<PedidoHasProduto> list = new ArrayList<>();
        String sql = "SELECT id_pedido, id_produto, preco_item FROM pedido_has_produto WHERE id_pedido = ?";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPedido);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    list.add(mapResultSetToPedidoHasProduto(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<PedidoHasProduto> findByProdutoId(Integer idProduto) {
        List<PedidoHasProduto> list = new ArrayList<>();
        String sql = "SELECT id_pedido, id_produto, preco_item FROM pedido_has_produto WHERE id_produto = ?";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idProduto);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    list.add(mapResultSetToPedidoHasProduto(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
