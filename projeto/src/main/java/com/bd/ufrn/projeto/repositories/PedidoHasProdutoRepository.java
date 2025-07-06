package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.models.PedidoHasProduto;
import com.bd.ufrn.projeto.services.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
// remover
@Repository
public class PedidoHasProdutoRepository extends AbstractRepository<PedidoHasProduto> {
    private final ConnectionFactory connectionFactory;

    @Autowired
    public PedidoHasProdutoRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public PedidoHasProduto findByPedidoIdAndProdutoId(Integer idPedido, Integer idProduto) {
        String sql = "SELECT id_pedido, id_produto, preco_item FROM pedido_has_produto WHERE id_pedido = ? AND id_produto = ?";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            ps.setInt(2, idProduto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPedidoHasProduto(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private PedidoHasProduto mapResultSetToPedidoHasProduto(ResultSet rs) throws SQLException {
        return PedidoHasProduto.builder()
                .idPedido(rs.getInt("id_pedido"))
                .idProduto(rs.getInt("id_produto"))
                .precoItem(rs.getDouble("preco_item"))
                .build();
    }

    @Override
    public void save(PedidoHasProduto pedidoHasProduto) {
        String sql = "INSERT INTO pedido_has_produto (id_pedido, id_produto, preco_item) VALUES (?, ?, ?) ";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, pedidoHasProduto.getIdPedido());
            ps.setInt(2, pedidoHasProduto.getIdProduto());
            ps.setDouble(3, pedidoHasProduto.getPrecoItem());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteByPedidoIdAndProdutoId(Integer idPedido, Integer idProduto) {
        String sql = "DELETE FROM pedido_has_produto WHERE id_pedido = ? AND id_produto = ?";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, idPedido);
            ps.setInt(2, idProduto);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(PedidoHasProduto entity) {
        if (entity.getIdPedido() == null || entity.getIdProduto() == null) {
            throw new IllegalArgumentException("Both idPedido and idProduto must be provided to delete a PedidoHasProduto entry.");
        }
        deleteByPedidoIdAndProdutoId(entity.getIdPedido(), entity.getIdProduto());
    }

    @Override
    public List<PedidoHasProduto> findAll() {
        List<PedidoHasProduto> list = new ArrayList<>();
        String sql = "SELECT id_pedido, id_produto, preco_item FROM pedido_has_produto";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToPedidoHasProduto(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<PedidoHasProduto> findByPedidoId(Integer idPedido) {
        List<PedidoHasProduto> list = new ArrayList<>();
        String sql = "SELECT id_pedido, id_produto, preco_item FROM pedido_has_produto WHERE id_pedido = ?";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToPedidoHasProduto(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<PedidoHasProduto> findByProdutoId(Integer idProduto) {
        List<PedidoHasProduto> list = new ArrayList<>();
        String sql = "SELECT id_pedido, id_produto, preco_item FROM pedido_has_produto WHERE id_produto = ?";
        try (Connection connection = connectionFactory.connection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idProduto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToPedidoHasProduto(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
