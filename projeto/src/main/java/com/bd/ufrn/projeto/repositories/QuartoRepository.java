package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.enums.TipoQuarto;
import com.bd.ufrn.projeto.interfaces.StrongEntity;
import com.bd.ufrn.projeto.models.Quarto;
import com.bd.ufrn.projeto.services.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class QuartoRepository extends AbstractRepository<Quarto> implements StrongEntity<Quarto, Integer> {
    private final ConnectionFactory connectionFactory;

    @Autowired
    public QuartoRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public List<Quarto> findByOcupado() {
        List<Quarto> quartos = new ArrayList<>();
        String sql = "SELECT * FROM quarto WHERE ocupado = ?";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBoolean(1, true);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    quartos.add(mapResultSetToQuarto(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quartos;
    }

    public List<Quarto> findByMarcado() {
        List<Quarto> quartos = new ArrayList<>();
        String sql = "SELECT * FROM quarto WHERE marcado_para_limpeza = ?";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBoolean(1, true);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    quartos.add(mapResultSetToQuarto(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quartos;
    }

    public List<Quarto> findByTipo(TipoQuarto tipo) {
        List<Quarto> quartos = new ArrayList<>();
        String sql = "SELECT * FROM quarto WHERE tipo LIKE ?";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tipo.name());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    quartos.add(mapResultSetToQuarto(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quartos;
    }

    @Override
    public Quarto findById(Integer numero) {
        Quarto quarto = null;
        String sql = "SELECT * FROM quarto WHERE numero = ?";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, numero);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    quarto = mapResultSetToQuarto(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quarto;
    }

    private Quarto mapResultSetToQuarto(ResultSet resultSet) throws SQLException {
        return Quarto.builder()
                .numero(resultSet.getInt("numero"))
                .naoPerturbe(resultSet.getBoolean("nao_perturbe"))
                .ocupado(resultSet.getBoolean("ocupado"))
                .marcadoPraLimpeza(resultSet.getBoolean("marcado_para_limpeza"))
                .tipo(TipoQuarto.valueOf(resultSet.getString("tipo")))
                .build();
    }


    @Override
    public void save(Quarto quarto) {
        String sql = "INSERT INTO quarto (numero, nao_perturbe, ocupado, marcado_para_limpeza, tipo) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, quarto.getNumero());
            preparedStatement.setBoolean(2, quarto.isNaoPerturbe());
            preparedStatement.setBoolean(3, quarto.isOcupado());
            preparedStatement.setBoolean(4, quarto.isMarcadoPraLimpeza());
            preparedStatement.setString(5, quarto.getTipo().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Quarto entity) {
        String sql = "DELETE FROM quarto WHERE numero = ?";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, entity.getNumero());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Quarto> findAll() {
        List<Quarto> quartos = new ArrayList<>();
        String sql = "SELECT * FROM quarto";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    quartos.add(mapResultSetToQuarto(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quartos;
    }
}