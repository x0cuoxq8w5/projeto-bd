package com.bd.ufrn.projeto.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bd.ufrn.projeto.interfaces.StrongEntity;
import com.bd.ufrn.projeto.models.Hospede;
import com.bd.ufrn.projeto.services.ConnectionFactory;

@Repository
public class HospedeRepository extends AbstractRepository<Hospede> implements StrongEntity<Hospede, String> {

    private final ConnectionFactory connectionFactory;

    @Autowired
    public HospedeRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Hospede findById(String cpf) { 
        String sql = "SELECT cpf, nome_sobrenome, data_nasc, desativado FROM hospede WHERE cpf = ?";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cpf); 
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
                .cpf(resultSet.getString("cpf"))
                .nome(resultSet.getString("nome_sobrenome"))
                .dataNascimento(resultSet.getTimestamp("data_nasc").toLocalDateTime())
                .desativado(resultSet.getBoolean("desativado"))
                .build();
    }

    @Override
    public void save(Hospede hospede) {
        String sql;
        boolean exists = findById(hospede.getCpf()) != null;

        if (exists) {
            sql = "UPDATE hospede SET nome_sobrenome = ?, data_nasc = ?, desativado = ? WHERE cpf = ?";
        } else {
            sql = "INSERT INTO hospede (nome_sobrenome, data_nasc, desativado, cpf) VALUES (?, ?, ?, ?)";
        }

        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, hospede.getNome());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(hospede.getDataNascimento()));
            preparedStatement.setBoolean(3, hospede.isDesativado());
            preparedStatement.setString(4, hospede.getCpf());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Hospede hospede) {
        String sql = "UPDATE hospede SET desativado = true WHERE cpf = ?";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, hospede.getCpf());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Hospede> findAll() {
        List<Hospede> hospedes = new ArrayList<>();
        String sql = "SELECT cpf, nome_sobrenome, data_nasc, desativado FROM hospede";
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

    
    public List<Hospede> findByDesativado(boolean desativado) {
        List<Hospede> hospedes = new ArrayList<>();
        String sql = "SELECT cpf, nome_sobrenome, data_nasc, desativado FROM hospede WHERE desativado = ?";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBoolean(1, desativado);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    hospedes.add(mapResultSetToHospede(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return hospedes;
    }
}
