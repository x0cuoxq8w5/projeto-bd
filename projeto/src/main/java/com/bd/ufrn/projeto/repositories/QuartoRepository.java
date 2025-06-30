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
import java.util.List;

@Repository
public class QuartoRepository extends AbstractRepository<Quarto> implements StrongEntity<Quarto,Integer> {
    private Connection connection;
    private Statement command;
    private ConnectionFactory connectionFactory;

    @Autowired
    public QuartoRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
    //SELECT * FROM quarto WHERE ocupado = true
    public List<Quarto> findByOcupado() {
        return null;
    }
    //SELECT * FROM quarto WHERE marcado_pra_limpeza = true
    public List<Quarto> findByMarcado() {
        return null;
    }
    //SELECT * FROM quarto WHERE tipo LIKE <tipo.name()>
    public List<Quarto> findByTipo(TipoQuarto tipo) {
        return null;
    }

    @Override
    public Quarto findById(Integer integer) {
        try {
            connect();
            String sql = "SELECT * FROM quarto WHERE numero = " + integer;
            ResultSet resultSet = command.executeQuery(sql);
            if (resultSet.next()) {
                return Quarto.builder()
                        .numero(resultSet.getInt("numero"))
                        .naoPerturbe(resultSet.getBoolean("nao_perturbe"))
                        .ocupado(resultSet.getBoolean("ocupado"))
                        .marcadoPraLimpeza(resultSet.getBoolean("marcado_pra_limpeza"))
                        .tipo(TipoQuarto.valueOf(resultSet.getString("tipo")))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return null;
    }

    public void connect () throws SQLException {
        connection = connectionFactory.connection();
        command = connection.createStatement();
    }

    public void save(Quarto quarto) {
        try {
            connect();

            StringBuffer buffer = new StringBuffer();
            buffer.append("INSERT INTO quarto (");
            buffer.append(this.getQuartoColums());
            buffer.append(") VALUES (");
            buffer.append(getQuartoValues(quarto));
            buffer.append(")");
            String sql = buffer.toString();
            command.executeUpdate(sql);
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getQuartoValues(Quarto quarto) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(quarto.getNumero());
        buffer.append(", ");
        buffer.append(quarto.isNaoPerturbe());
        buffer.append(", ");
        buffer.append(quarto.isOcupado());
        buffer.append(", ");
        buffer.append(quarto.isMarcadoPraLimpeza());
        buffer.append(", '");
        buffer.append(quarto.getTipo().toString());
        buffer.append("'");
        return buffer.toString();
    }

    private String getQuartoColums() {
        return "numero, nao_perturbe, ocupado, marcado_para_limpeza, tipo";
    }

    public void closeConnection() {
        try {
            command.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Quarto entity) {

    }

    @Override
    public List<Quarto> findAll() {
        return List.of();
    }
}
