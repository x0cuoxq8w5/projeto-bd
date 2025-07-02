package com.bd.ufrn.projeto.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PedidoHasProduto {
    Integer idPedido;
    Integer idProduto;
    Double precoItem;
}
