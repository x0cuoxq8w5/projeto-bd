package com.bd.ufrn.projeto.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReservaFormReq {

    private String nome;
    private String cpf;
    private String tipoQuarto;
    private Long quartoId;

    public ReservaFormReq() {
    }

    @Override
    public String toString() {
        return "ReservaRequest{" +
                "nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", tipoQuarto='" + tipoQuarto + '\'' +
                ", quartoId=" + quartoId +
                '}';
    }
}