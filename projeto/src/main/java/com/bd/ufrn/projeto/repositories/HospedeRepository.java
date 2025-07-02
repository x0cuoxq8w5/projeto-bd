package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.interfaces.StrongEntity;
import com.bd.ufrn.projeto.models.Hospede;
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
public class HospedeRepository extends AbstractRepository<Hospede> implements StrongEntity<Hospede, Integer> {
    private final ConnectionFactory connectionFactory;

    @Autowired
    public HospedeRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    // Retrieve a Hospede by CPF
    @Override
    public Hospede findById(Integer cpf) {
        Hospede hospede = null;
        String sql = "SELECT p.* FROM pessoa p INNER JOIN hospede h ON p.cpf = h.cpf WHERE p.cpf = ?";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, cpf);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    hospede = mapResultSetToHospede(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hospede;
    }

    // Helper method to convert ResultSet into a Hospede object
    private Hospede mapResultSetToHospede(ResultSet resultSet) throws SQLException {
        Hospede hospede = Hospede.builder().build();
        hospede.setCpf(resultSet.getInt("cpf"));
        hospede.setNome(resultSet.getString("nome_sobrenome"));
        hospede.setDataNascimento(resultSet.getTimestamp("data_nasc").toLocalDateTime());
        return hospede;
    }

    // Save a new Hospede (and corresponding Pessoa) to the database
    @Override
    public void save(Hospede hospede) {
        Connection connection = null;
        try {
            connection = connectionFactory.connection();
            connection.setAutoCommit(false);

            // Insert into 'pessoa' table
            String pessoaSql = "INSERT INTO pessoa (cpf, nome_sobrenome, data_nasc) VALUES (?, ?, ?)";
            try (PreparedStatement pessoaStatement = connection.prepareStatement(pessoaSql)) {
                pessoaStatement.setInt(1, hospede.getCpf());
                pessoaStatement.setString(2, hospede.getNome());
                pessoaStatement.setTimestamp(3, Timestamp.valueOf(hospede.getDataNascimento()));
                pessoaStatement.executeUpdate();
            }

            // Insert into 'hospede' table
            String hospedeSql = "INSERT INTO hospede (cpf) VALUES (?)";
            try (PreparedStatement hospedeStatement = connection.prepareStatement(hospedeSql)) {
                hospedeStatement.setInt(1, hospede.getCpf());
                hospedeStatement.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            // Roll back in case of error
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            // Clean up and reset connection state
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

    // Delete a Hospede (and associated Pessoa) from the database
    @Override
    public void delete(Hospede entity) {
        Connection connection = null;
        try {
            connection = connectionFactory.connection();
            connection.setAutoCommit(false);

            // Delete from 'hospede' first (due to FK constraints)
            String hospedeSql = "DELETE FROM hospede WHERE cpf = ?";
            try (PreparedStatement hospedeStatement = connection.prepareStatement(hospedeSql)) {
                hospedeStatement.setInt(1, entity.getCpf());
                hospedeStatement.executeUpdate();
            }

            // Then delete from 'pessoa'
            String pessoaSql = "DELETE FROM pessoa WHERE cpf = ?";
            try (PreparedStatement pessoaStatement = connection.prepareStatement(pessoaSql)) {
                pessoaStatement.setInt(1, entity.getCpf());
                pessoaStatement.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            // Roll back in case of error
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            // Clean up and reset connection state
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

    // Retrieve all Hospedes from the database
    @Override
    public List<Hospede> findAll() {
        List<Hospede> hospedes = new ArrayList<>();
        String sql = "SELECT p.* FROM pessoa p INNER JOIN hospede h ON p.cpf = h.cpf";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    hospedes.add(mapResultSetToHospede(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hospedes;
    }
}
