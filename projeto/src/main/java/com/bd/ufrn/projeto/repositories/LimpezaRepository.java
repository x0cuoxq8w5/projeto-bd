package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.models.Limpeza;
import com.bd.ufrn.projeto.models.PedidoHasProduto;

import java.util.List;

public class LimpezaRepository extends AbstractRepository<Limpeza>{
    //SELECT * FROM pedido_has_produto WHERE id_pedido = <idPedido> AND id_produto = <idProduto>
    public PedidoHasProduto findByIds(Integer idPedido, Integer idProduto) {
        return null;
    }
    //SELECT * FROM pedido_has_produto WHERE id_produto = <idProduto>
    public List<PedidoHasProduto> findByProdutoId(Integer idProduto) {
        return null;
    }
    //SELECT * FROM pedido_has_produto WHERE id_pedido = <idPedido>
    public List<PedidoHasProduto> findByPedidoId(Integer idPedido) {
        return null;
    }

    @Override
    public void save(Limpeza entity) {

    }

    @Override
    public void delete(Limpeza entity) {

    }

    @Override
    public List<Limpeza> findAll() {
        return List.of();
    }
}
