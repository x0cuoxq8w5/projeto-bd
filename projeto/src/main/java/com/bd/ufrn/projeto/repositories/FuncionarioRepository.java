package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.enums.Papel;
import com.bd.ufrn.projeto.interfaces.StrongEntity;
import com.bd.ufrn.projeto.models.Funcionario;
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
public class FuncionarioRepository extends AbstractRepository<Funcionario> implements StrongEntity<Funcionario, Integer> {
    private final ConnectionFactory connectionFactory;

    @Autowired
    public FuncionarioRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Funcionario findById(Integer cpf) {
        Funcionario funcionario = null;
        String sql = "SELECT f.* FROM funcionario f WHERE f.cpf = ?";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, cpf);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    funcionario = mapResultSetToFuncionario(resultSet);
                    // Fetch papeis for funcionario
                    funcionario.setPapeis(findPapeisByCpf(connection, funcionario.getCpf()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return funcionario;
    }

    private List<Papel> findPapeisByCpf(Connection connection, String cpf) throws SQLException {
        List<Papel> papeis = new ArrayList<>();
        String sql = "SELECT papel FROM papel WHERE cpf = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cpf);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    papeis.add(Papel.valueOf(resultSet.getString("papel")));
                }
            }
        }
        return papeis;
    }

    private Funcionario mapResultSetToFuncionario(ResultSet resultSet) throws SQLException {
        return Funcionario.builder()
                .cpf(resultSet.getString("cpf"))
                .nome(resultSet.getString("nome_sobrenome"))
                .dataNascimento(resultSet.getTimestamp("data_nasc").toLocalDateTime())
                .numFuncionario(resultSet.getInt("num_funcionario"))
                .administrador(resultSet.getBoolean("administrador"))
                .build();
    }

    @Override
    public void save(Funcionario funcionario) {
        try (Connection connection = connectionFactory.connection()) {
            connection.setAutoCommit(false);

            // Save Funcionario
            String funcionarioSql = "INSERT INTO funcionario (cpf, nome_sobrenome, data_nasc, num_funcionario, administrador) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement funcionarioStatement = connection.prepareStatement(funcionarioSql)) {
                funcionarioStatement.setString(1, funcionario.getCpf());
                funcionarioStatement.setString(2, funcionario.getNome());
                funcionarioStatement.setTimestamp(3, Timestamp.valueOf(funcionario.getDataNascimento()));
                funcionarioStatement.setInt(4, funcionario.getNumFuncionario());
                funcionarioStatement.setBoolean(5, funcionario.isAdministrador());
                funcionarioStatement.executeUpdate();
            }

            // Save Papeis
            if (funcionario.getPapeis() != null && !funcionario.getPapeis().isEmpty()) {
                String papelSql = "INSERT INTO papel (cpf, papel) VALUES (?, ?)";
                try (PreparedStatement papelStatement = connection.prepareStatement(papelSql)) {
                    for (Papel papel : funcionario.getPapeis()) {
                        papelStatement.setString(1, funcionario.getCpf());
                        papelStatement.setString(2, papel.name());
                        papelStatement.addBatch();
                    }
                    papelStatement.executeBatch();
                }
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void delete(Funcionario entity) {
        Connection connection = null;
        try {
            connection = connectionFactory.connection();
            connection.setAutoCommit(false);

            // Delete Papeis
            String deletePapelSql = "DELETE FROM papel WHERE cpf = ?";
            try (PreparedStatement deletePapelStatement = connection.prepareStatement(deletePapelSql)) {
                deletePapelStatement.setString(1, entity.getCpf());
                deletePapelStatement.executeUpdate();
            }

            // Delete Funcionario
            String deleteFuncionarioSql = "DELETE FROM funcionario WHERE cpf = ?";
            try (PreparedStatement deleteFuncionarioStatement = connection.prepareStatement(deleteFuncionarioSql)) {
                deleteFuncionarioStatement.setString(1, entity.getCpf());
                deleteFuncionarioStatement.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }


    @Override
    public List<Funcionario> findAll() {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT cpf, nome_sobrenome, data_nasc, num_funcionario, administrador FROM funcionario";

        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Funcionario funcionario = mapResultSetToFuncionario(resultSet);

                // Fetch and set papeis
                funcionario.setPapeis(findPapeisByCpf(connection, funcionario.getCpf()));

                funcionarios.add(funcionario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return funcionarios;
    }

    //SELECT * FROM (funcionario LEFT OUTER JOIN equipe_limpeza_has_funionario) WHERE cpf = <cpf>
    //Se encontrar retorna true caso contrário false
    public boolean isCleaner() {
        return false;
    }

    //SELECT * FROM (funcionario NATURAL JOIN papel) WHERE papel = <papel>

    public List<Funcionario> findByPapel(Papel papel) {
        return null;
    }

    //Talvez criar uma pra achar funcionários sem papel?
    //Ou só não permitir funcionario que não tenha papel

}