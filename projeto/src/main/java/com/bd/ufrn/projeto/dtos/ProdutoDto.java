package com.bd.ufrn.projeto.dtos;

import com.bd.ufrn.projeto.models.Produto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoDto {
    private int id;
    private String nome;
    private double precoAtual;
    private int quantidade;

    public ProdutoDto(Produto p) {
        this.id = p.getId();
        this.nome = p.getNome();
        this.precoAtual = p.getPrecoAtual();
        this.quantidade = p.getQuantidade();
    }
}
