package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.interfaces.StrongEntity;
import com.bd.ufrn.projeto.models.Hospede;
import com.bd.ufrn.projeto.models.Pedido;
import com.bd.ufrn.projeto.models.Reserva;
import com.bd.ufrn.projeto.services.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
@Repository
public class PedidoRepository extends AbstractRepository<Pedido> implements StrongEntity<Pedido,Integer> {
    private final ConnectionFactory connectionFactory;

    @Autowired
    public PedidoRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public List<Pedido> findByCpf(Integer cpf) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT id_pedido, data_pedido, data_entega, cpf FROM pedido WHERE cpf = ?";
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
        }
        return pedidos;
    }

    @Override
    public Pedido findById(Integer id) {
        Pedido pedido = null;
        String sql = "SELECT id_pedido, data_pedido, data_entega, cpf FROM pedido WHERE id_pedido = ?";
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
        return Pedido.builder()
                .id(resultSet.getInt("id_pedido"))
                .dataPedido(resultSet.getTimestamp("data_pedido").toLocalDateTime())
                .dataEntrega(resultSet.getTimestamp("data_entega") != null ? resultSet.getTimestamp("data_entega").toLocalDateTime() : null)
                .cpfHospede(resultSet.getInt("cpf"))
                .build();
    }

    @Override
    public void save(Pedido pedido) {
        Connection connection = null;
        try {
            connection = connectionFactory.connection();
            connection.setAutoCommit(false);

            String sql = "INSERT INTO pedido (id_pedido, data_pedido, data_entega, cpf) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, pedido.getId());
                preparedStatement.setTimestamp(2, Timestamp.valueOf(pedido.getDataPedido()));

                if (pedido.getDataEntrega() != null) {
                    preparedStatement.setTimestamp(3, Timestamp.valueOf(pedido.getDataEntrega()));
                } else {
                    preparedStatement.setNull(3, java.sql.Types.TIMESTAMP);
                }
                preparedStatement.setInt(4, pedido.getCpfHospede());
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
    public void delete(Pedido entity) {
        Connection connection = null;
        try {
            connection = connectionFactory.connection();
            connection.setAutoCommit(false);

            String sql = "DELETE FROM pedido WHERE id_pedido = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, entity.getId());
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
    public List<Pedido> findAll() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT id_pedido, data_pedido, data_entega, cpf FROM pedido";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    pedidos.add(mapResultSetToPedido(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }
}
