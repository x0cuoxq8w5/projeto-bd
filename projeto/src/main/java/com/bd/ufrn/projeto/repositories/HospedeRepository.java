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
import java.util.Collections;
import java.util.List;

@Repository
public class HospedeRepository extends AbstractRepository<Hospede> implements StrongEntity<Hospede, Integer> {

    private final ConnectionFactory connectionFactory;

    @Autowired
    public HospedeRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Hospede findById(Integer cpf) {
        String sql = "SELECT cpf, nome_sobrenome, data_nasc FROM hospede WHERE cpf = ?";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, cpf);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToHospede(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Hospede mapResultSetToHospede(ResultSet resultSet) throws SQLException {
        return Hospede.builder()
                .cpf(resultSet.getInt("cpf"))
                .nome(resultSet.getString("nome_sobrenome"))
                .dataNascimento(resultSet.getTimestamp("data_nasc").toLocalDateTime())
                .build();
    }

    @Override
    public void save(Hospede hospede) {
        String sql = "INSERT INTO hospede (cpf, nome_sobrenome, data_nasc) VALUES (?, ?, ?)";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, hospede.getCpf());
            preparedStatement.setString(2, hospede.getNome());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(hospede.getDataNascimento()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Hospede hospede) {
        String sql = "DELETE FROM hospede WHERE cpf = ?";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, hospede.getCpf());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Hospede> findAll() {
        List<Hospede> hospedes = new ArrayList<>();
        String sql = "SELECT cpf, nome_sobrenome, data_nasc FROM hospede";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                hospedes.add(mapResultSetToHospede(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return hospedes;
    }
}
