package com.bd.ufrn.projeto.models;

import com.bd.ufrn.projeto.enums.Papel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Builder
public class Funcionario extends Pessoa{
    int numFuncionario;
    boolean administrador;
    List<Papel> papeis;
}
