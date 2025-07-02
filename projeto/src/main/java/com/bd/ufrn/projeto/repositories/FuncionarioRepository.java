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
        String sql = "SELECT p.*, f.num_funcionario, f.administrador FROM pessoa p INNER JOIN funcionario f ON p.cpf = f.cpf WHERE p.cpf = ?";
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

    private List<Papel> findPapeisByCpf(Connection connection, int cpf) throws SQLException {
        List<Papel> papeis = new ArrayList<>();
        String sql = "SELECT papel FROM papel WHERE cpf = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, cpf);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    papeis.add(Papel.valueOf(resultSet.getString("papel")));
                }
            }
        }
        return papeis;
    }

    private Funcionario mapResultSetToFuncionario(ResultSet resultSet) throws SQLException {
        Funcionario funcionario = Funcionario.builder()
                .numFuncionario(resultSet.getInt("num_funcionario"))
                .administrador(resultSet.getBoolean("administrador"))
                .build();
        funcionario.setCpf(resultSet.getInt("cpf"));
        funcionario.setNome(resultSet.getString("nome_sobrenome"));
        funcionario.setDataNascimento(resultSet.getTimestamp("data_nasc").toLocalDateTime());
        return funcionario;
    }

    @Override
    public void save(Funcionario funcionario) {
        Connection connection = null;
        try {
            connection = connectionFactory.connection();
            connection.setAutoCommit(false);

            // Save Pessoa data
            String pessoaSql = "INSERT INTO pessoa (cpf, nome_sobrenome, data_nasc) VALUES (?, ?, ?)";
            try (PreparedStatement pessoaStatement = connection.prepareStatement(pessoaSql)) {
                pessoaStatement.setInt(1, funcionario.getCpf());
                pessoaStatement.setString(2, funcionario.getNome());
                pessoaStatement.setTimestamp(3, Timestamp.valueOf(funcionario.getDataNascimento()));
                pessoaStatement.executeUpdate();
            }

            // Save Funcionario data
            String funcionarioSql = "INSERT INTO funcionario (cpf, num_funcionario, administrador) VALUES (?, ?, ?)";
            try (PreparedStatement funcionarioStatement = connection.prepareStatement(funcionarioSql)) {
                funcionarioStatement.setInt(1, funcionario.getCpf());
                funcionarioStatement.setInt(2, funcionario.getNumFuncionario());
                funcionarioStatement.setBoolean(3, funcionario.isAdministrador());
                funcionarioStatement.executeUpdate();
            }

            // Save Funcionario's Papeis
            if (funcionario.getPapeis() != null && !funcionario.getPapeis().isEmpty()) {
                String papelSql = "INSERT INTO papel (cpf, papel) VALUES (?, ?)";
                try (PreparedStatement papelStatement = connection.prepareStatement(papelSql)) {
                    for (Papel papel : funcionario.getPapeis()) {
                        papelStatement.setInt(1, funcionario.getCpf());
                        papelStatement.setString(2, papel.name());
                        papelStatement.addBatch();
                    }
                    papelStatement.executeBatch();
                }
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
    public void delete(Funcionario entity) {
        Connection connection = null;
        try {
            connection = connectionFactory.connection();
            connection.setAutoCommit(false);

            // Delete Funcionario's Papeis first
            String deletePapelSql = "DELETE FROM papel WHERE cpf = ?";
            try (PreparedStatement deletePapelStatement = connection.prepareStatement(deletePapelSql)) {
                deletePapelStatement.setInt(1, entity.getCpf());
                deletePapelStatement.executeUpdate();
            }

            // Delete Funcionario data
            String funcionarioSql = "DELETE FROM funcionario WHERE cpf = ?";
            try (PreparedStatement funcionarioStatement = connection.prepareStatement(funcionarioSql)) {
                funcionarioStatement.setInt(1, entity.getCpf());
                funcionarioStatement.executeUpdate();
            }

            // Delete Pessoa data
            String pessoaSql = "DELETE FROM pessoa WHERE cpf = ?";
            try (PreparedStatement pessoaStatement = connection.prepareStatement(pessoaSql)) {
                pessoaStatement.setInt(1, entity.getCpf());
                pessoaStatement.executeUpdate();
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
    public List<Funcionario> findAll() {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT p.*, f.num_funcionario, f.administrador FROM pessoa p INNER JOIN funcionario f ON p.cpf = f.cpf";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Funcionario funcionario = mapResultSetToFuncionario(resultSet);
                    // Fetch papeis for each funcionario
                    funcionario.setPapeis(findPapeisByCpf(connection, funcionario.getCpf()));
                    funcionarios.add(funcionario);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return funcionarios;
    }
}