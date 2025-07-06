package com.bd.ufrn.projeto.dtos;

import com.bd.ufrn.projeto.models.Produto;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ItemPedidoDTO {
    private Produto produto;
    private double preco;
    private int quantidade;
}
