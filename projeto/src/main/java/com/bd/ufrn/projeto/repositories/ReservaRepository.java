package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.enums.TipoQuarto;
import com.bd.ufrn.projeto.interfaces.StrongEntity;
import com.bd.ufrn.projeto.models.Hospede;
import com.bd.ufrn.projeto.models.Quarto;
import com.bd.ufrn.projeto.models.Reserva;
import com.bd.ufrn.projeto.services.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservaRepository extends AbstractRepository<Reserva> implements StrongEntity<Reserva, Integer> {

    private final ConnectionFactory connectionFactory;

    @Autowired
    public ReservaRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Reserva findById(Integer id) {
        String sql = """
            SELECT r.*, h.nome_sobrenome, h.data_nasc,
                   q.tipo, q.nao_perturbe, q.ocupado, q.marcado_para_limpeza
            FROM reserva r
            INNER JOIN hospede h ON r.cpf = h.cpf
            INNER JOIN quarto q ON r.numero = q.numero
            WHERE r.id_reserva = ?
        """;

        try (Connection conn = connectionFactory.connection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReserva(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Reserva> findByHospede(Hospede hospede) {
        return findByCpf(hospede.getCpf());
    }

    public List<Integer> findQuartosOcupados(LocalDateTime dataInicio, LocalDateTime dataFim) {
        String sql = """
            SELECT DISTINCT r.numero
            FROM reserva r
            WHERE r.data_inicio < ? AND r.data_fim > ?
        """;

        List<Integer> quartosOcupadosIds = new ArrayList<>();

        try (Connection conn = connectionFactory.connection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(dataFim));
            stmt.setTimestamp(2, Timestamp.valueOf(dataInicio));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    quartosOcupadosIds.add(rs.getInt("numero"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quartosOcupadosIds;
    }

    public List<Reserva> findByCpf(String cpf) {
        String sql = """
            SELECT r.*, h.nome_sobrenome, h.data_nasc,
                   q.tipo, q.nao_perturbe, q.ocupado, q.marcado_para_limpeza
            FROM reserva r
            INNER JOIN hospede h ON r.cpf = h.cpf
            INNER JOIN quarto q ON r.numero = q.numero
            WHERE r.cpf = ?
            ORDER BY r.data_inicio
        """;

        List<Reserva> reservas = new ArrayList<>();

        try (Connection conn = connectionFactory.connection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapResultSetToReserva(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservas;
    }

    @Override
    public List<Reserva> findAll() {
        String sql = """
            SELECT r.*, h.nome_sobrenome, h.data_nasc,
                   q.tipo, q.nao_perturbe, q.ocupado, q.marcado_para_limpeza
            FROM reserva r
            INNER JOIN hospede h ON r.cpf = h.cpf
            INNER JOIN quarto q ON r.numero = q.numero
            ORDER BY r.id_reserva
        """;

        List<Reserva> reservas = new ArrayList<>();

        try (Connection conn = connectionFactory.connection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                reservas.add(mapResultSetToReserva(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservas;
    }

    @Override
    public void save(Reserva reserva) {
        boolean isInsert = (reserva.getId() == null);

        String insertSql = """
            INSERT INTO reserva (cpf, data_inicio, data_fim, data_entrada, data_saida, numero)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        String updateSql = """
            UPDATE reserva
            SET cpf = ?, data_inicio = ?, data_fim = ?, data_entrada = ?, data_saida = ?, numero = ?
            WHERE id_reserva = ?
        """;

        try (Connection conn = connectionFactory.connection()) {

            if (isInsert) {
                try (PreparedStatement stmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                    setStatementParams(stmt, reserva, false);
                    stmt.executeUpdate();

                    try (ResultSet keys = stmt.getGeneratedKeys()) {
                        if (keys.next()) {
                            reserva.setId(keys.getInt(1));
                        }
                    }
                }
            } else {
                try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                    setStatementParams(stmt, reserva, true);
                    stmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save reserva", e);
        }
    }

    private void setStatementParams(PreparedStatement stmt, Reserva r, boolean includeId) throws SQLException {
        stmt.setString(1, r.getHospede().getCpf());
        stmt.setTimestamp(2, toTimestamp(r.getDataInicio()));
        stmt.setTimestamp(3, toTimestamp(r.getDataFim()));
        stmt.setTimestamp(4, toTimestamp(r.getDataEntrada()));
        stmt.setTimestamp(5, toTimestamp(r.getDataSaida()));
        stmt.setInt(6, r.getQuarto().getNumero());

        if (includeId) {
            stmt.setInt(7, r.getId());
        }
    }

    private Reserva mapResultSetToReserva(ResultSet rs) throws SQLException {
        Hospede hospede = Hospede.builder()
                .cpf(rs.getString("cpf"))
                .nome(rs.getString("nome_sobrenome"))
                .dataNascimento(rs.getTimestamp("data_nasc") != null ? rs.getTimestamp("data_nasc").toLocalDateTime() : null)
                .build();

        Quarto quarto = Quarto.builder()
                .numero(rs.getInt("numero"))
                .tipo(TipoQuarto.valueOf(rs.getString("tipo")))
                .ocupado(rs.getBoolean("ocupado"))
                .naoPerturbe(rs.getBoolean("nao_perturbe"))
                .marcadoParaLimpeza(rs.getBoolean("marcado_para_limpeza"))
                .build();

        return Reserva.builder()
                .id(rs.getInt("id_reserva"))
                .hospede(hospede)
                .quarto(quarto)
                .dataInicio(toLDT(rs.getTimestamp("data_inicio")))
                .dataFim(toLDT(rs.getTimestamp("data_fim")))
                .dataEntrada(toLDT(rs.getTimestamp("data_entrada")))
                .dataSaida(toLDT(rs.getTimestamp("data_saida")))
                .build();
    }

    private LocalDateTime toLDT(Timestamp ts) {
        return ts != null ? ts.toLocalDateTime() : null;
    }

    private Timestamp toTimestamp(LocalDateTime ldt) {
        return ldt != null ? Timestamp.valueOf(ldt) : null;
    }

    @Override
    public void delete(Reserva reserva) {
        if (reserva == null || reserva.getId() == null) {
            throw new IllegalArgumentException("Reserva or ID must not be null");
        }

        String sql = "DELETE FROM reserva WHERE id_reserva = ?";

        try (Connection conn = connectionFactory.connection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reserva.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete reserva", e);
        }
    }

    
}
