package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.enums.TipoQuarto;
import com.bd.ufrn.projeto.interfaces.StrongEntity;
import com.bd.ufrn.projeto.models.Quarto;
import com.bd.ufrn.projeto.services.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class QuartoRepository extends AbstractRepository<Quarto> implements StrongEntity<Quarto, Integer> {
    private Connection connection;
    private Statement command;
    private final ConnectionFactory connectionFactory;

    @Autowired
    public QuartoRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public List<Quarto> findByOcupado() {
        return executeSelectQuery("SELECT * FROM quarto WHERE ocupado = true");
    }

    public List<Quarto> findByMarcado() {
        return executeSelectQuery("SELECT * FROM quarto WHERE marcado_para_limpeza = true");
    }

    public List<Quarto> findByTipo(TipoQuarto tipo) {
        return executeSelectQuery("SELECT * FROM quarto WHERE tipo LIKE '" + tipo.name() + "'");
    }

    @Override
    public Quarto findById(Integer numero) {
        Quarto quarto = null;
        try {
            connect();
            String sql = "SELECT * FROM quarto WHERE numero = " + numero;
            ResultSet resultSet = command.executeQuery(sql);
            if (resultSet.next()) {
                quarto = mapResultSetToQuarto(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return quarto;
    }

    private List<Quarto> executeSelectQuery(String sql) {
        List<Quarto> quartos = new ArrayList<>();
        try {
            connect();
            ResultSet resultSet = command.executeQuery(sql);
            while (resultSet.next()) {
                quartos.add(mapResultSetToQuarto(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return quartos;
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

    public void connect() throws SQLException {
        connection = connectionFactory.connection();
        command = connection.createStatement();
    }

    public void closeConnection() {
        try {
            if (command != null) command.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(Quarto quarto) {
        try {
            connect();
            String sql = String.format("INSERT INTO quarto (%s) VALUES (%s)", getQuartoColumns(), getQuartoValues(quarto));
            command.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private String getQuartoValues(Quarto quarto) {
        return String.format("%d, %b, %b, %b, '%s'",
                quarto.getNumero(),
                quarto.isNaoPerturbe(),
                quarto.isOcupado(),
                quarto.isMarcadoPraLimpeza(),
                quarto.getTipo().toString());
    }

    private String getQuartoColumns() {
        return "numero, nao_perturbe, ocupado, marcado_para_limpeza, tipo";
    }

    @Override
    public void delete(Quarto entity) {
        try {
            connect();
            String sql = "DELETE FROM quarto WHERE numero = " + entity.getNumero();
            command.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    @Override
    public List<Quarto> findAll() {
        return executeSelectQuery("SELECT * FROM quarto");
    }
}
