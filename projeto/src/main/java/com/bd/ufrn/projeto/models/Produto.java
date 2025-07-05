package com.bd.ufrn.projeto.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Produto {
    private int id;
    private double precoAtual;
    private int quantidade;
}
