package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.models.PedidoHasProduto;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PedidoHasProdutoRepository extends AbstractRepository<PedidoHasProduto> {
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
    public void save(PedidoHasProduto entity) {

    }

    @Override
    public void delete(PedidoHasProduto entity) {

    }

    @Override
    public List<PedidoHasProduto> findAll() {
        return List.of();
    }
}
