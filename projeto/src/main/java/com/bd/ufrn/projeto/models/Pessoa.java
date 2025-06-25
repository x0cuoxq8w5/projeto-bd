package com.bd.ufrn.projeto.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public abstract class Pessoa {
    int cpf;
    LocalDateTime dataNascimento;
    String nome;
}
