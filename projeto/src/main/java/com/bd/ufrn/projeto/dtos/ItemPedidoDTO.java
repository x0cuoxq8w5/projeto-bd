package com.bd.ufrn.projeto.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemPedidoDTO {
    private Integer produtoId;
    private String produtoNome;
    private int quantidade;
    private double preco;
}
