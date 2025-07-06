package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.enums.TipoQuarto;
import com.bd.ufrn.projeto.interfaces.StrongEntity;
import com.bd.ufrn.projeto.models.EquipeLimpeza;
import com.bd.ufrn.projeto.models.Limpeza;
import com.bd.ufrn.projeto.models.Quarto;

import com.bd.ufrn.projeto.services.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class LimpezaRepository extends AbstractRepository<Limpeza> implements StrongEntity<Limpeza, Integer> {

    private final ConnectionFactory connectionFactory;

    @Autowired
    public LimpezaRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Limpeza findById(Integer id) {
        String sql = """
            SELECT l.id, l.id_equipe, l.numero, l.data_limpeza,
                   q.nao_perturbe, q.ocupado, q.marcado_para_limpeza, q.tipo
            FROM limpeza l
            INNER JOIN quarto q ON l.numero = q.numero
            WHERE l.id = ?
        """;

        try (Connection connection = connectionFactory.connection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSetToLimpeza(rs) : null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch Limpeza by ID", e);
        }
    }

    @Override
    public List<Limpeza> findAll() {
        String sql = """
            SELECT l.id, l.id_equipe, l.numero, l.data_limpeza,
                   q.nao_perturbe, q.ocupado, q.marcado_para_limpeza, q.tipo
            FROM limpeza l
            INNER JOIN quarto q ON l.numero = q.numero
            ORDER BY l.id
        """;

        List<Limpeza> limpezas = new ArrayList<>();

        try (Connection connection = connectionFactory.connection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                limpezas.add(mapResultSetToLimpeza(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch Limpezas", e);
        }

        return limpezas;
    }

    @Override
    public void save(Limpeza limpeza) {
        boolean isInsert = (limpeza.getId() == null);

        String insertSql = """
        INSERT INTO limpeza (id_equipe, numero, data_limpeza)
        VALUES (?, ?, ?)
    """;

        String updateSql = """
        UPDATE limpeza
        SET id_equipe = ?, numero = ?, data_limpeza = ?
        WHERE id = ?
    """;

        try (Connection connection = connectionFactory.connection()) {
            if (isInsert) {
                try (PreparedStatement stmt = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, limpeza.getEquipeLimpeza().getIdEquipe());
                    stmt.setInt(2, limpeza.getQuarto().getNumero());
                    stmt.setTimestamp(3, toTimestamp(limpeza.getData()));
                    stmt.executeUpdate();

                    try (ResultSet keys = stmt.getGeneratedKeys()) {
                        if (keys.next()) {
                            limpeza.setId(keys.getInt(1));
                        } else {
                            throw new SQLException("Failed to retrieve ID for new Limpeza.");
                        }
                    }
                }
            } else {
                try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
                    stmt.setInt(1, limpeza.getEquipeLimpeza().getIdEquipe());
                    stmt.setInt(2, limpeza.getQuarto().getNumero());
                    stmt.setTimestamp(3, toTimestamp(limpeza.getData()));
                    stmt.setInt(4, limpeza.getId());
                    stmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save Limpeza", e);
        }
    }


    @Override
    public void delete(Limpeza limpeza) {
        if (limpeza == null || limpeza.getId() == null) {
            throw new IllegalArgumentException("Limpeza or its ID must not be null.");
        }

        String sql = "DELETE FROM limpeza WHERE id = ?";

        try (Connection connection = connectionFactory.connection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, limpeza.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete Limpeza", e);
        }
    }


    private Limpeza mapResultSetToLimpeza(ResultSet rs) throws SQLException {
        EquipeLimpeza equipe = EquipeLimpeza.builder()
                .idEquipe(rs.getInt("id_equipe"))
                .build();

        Quarto quarto = Quarto.builder()
                .numero(rs.getInt("numero"))
                .naoPerturbe(rs.getBoolean("nao_perturbe"))
                .ocupado(rs.getBoolean("ocupado"))
                .marcadoParaLimpeza(rs.getBoolean("marcado_para_limpeza"))
                .tipo(TipoQuarto.valueOf(rs.getString("tipo")))
                .build();

        return Limpeza.builder()
                .id(rs.getInt("id"))
                .equipeLimpeza(equipe)
                .quarto(quarto)
                .data(rs.getTimestamp("data_limpeza").toLocalDateTime())
                .build();
    }

    private Timestamp toTimestamp(LocalDateTime dateTime) {
        return dateTime != null ? Timestamp.valueOf(dateTime) : null;
    }
}
