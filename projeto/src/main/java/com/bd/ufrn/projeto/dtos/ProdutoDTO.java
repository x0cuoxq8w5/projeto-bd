package com.bd.ufrn.projeto.dtos;

public record ProdutoDTO(
        Integer id,
        String nome,
        Double precoAtual,
        Integer quantidade
) {
}
