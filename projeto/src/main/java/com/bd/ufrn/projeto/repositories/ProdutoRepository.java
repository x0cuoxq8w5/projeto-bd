package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.interfaces.StrongEntity;
import com.bd.ufrn.projeto.models.Produto;
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
public class ProdutoRepository extends AbstractRepository<Produto> implements StrongEntity<Produto,Integer> {
    private final ConnectionFactory connectionFactory;

    @Autowired
    public ProdutoRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public List<Produto> findUnstocked() {
        List<Produto> unstockedProducts = new ArrayList<>();
        String sql = "SELECT id_produto, preco_atual, quantidade FROM produto WHERE quantidade = 0";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    unstockedProducts.add(mapResultSetToProduto(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return unstockedProducts;
    }

    @Override
    public Produto findById(Integer id) {
        Produto produto = null;
        String sql = "SELECT id_produto, preco_atual, quantidade FROM produto WHERE id_produto = ?";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    produto = mapResultSetToProduto(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produto;
    }

    private Produto mapResultSetToProduto(ResultSet resultSet) throws SQLException {
        return Produto.builder()
                .id(resultSet.getInt("id_produto"))
                .preco_atual(resultSet.getDouble("preco_atual"))
                .quantidade(resultSet.getInt("quantidade"))
                .build();
    }

    @Override
    public void save(Produto produto) {
        Connection connection = null;
        try {
            connection = connectionFactory.connection();
            connection.setAutoCommit(false);

            String sql = "INSERT INTO produto (id_produto, preco_atual, quantidade) VALUES (?, ?, ?) ";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, produto.getId());
                preparedStatement.setDouble(2, produto.getPreco_atual());
                preparedStatement.setInt(3, produto.getQuantidade());
                preparedStatement.executeUpdate();
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
    public void delete(Produto entity) {
        Connection connection = null;
        try {
            connection = connectionFactory.connection();
            connection.setAutoCommit(false);

            String sql = "DELETE FROM produto WHERE id_produto = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, entity.getId());
                preparedStatement.executeUpdate();
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
    public List<Produto> findAll() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT id_produto, preco_atual, quantidade FROM produto";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    produtos.add(mapResultSetToProduto(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produtos;
    }
}
