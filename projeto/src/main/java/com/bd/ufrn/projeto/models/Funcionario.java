package com.bd.ufrn.projeto.models;

import com.bd.ufrn.projeto.enums.Papel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
@Getter
@Setter
@SuperBuilder
public class Funcionario extends Pessoa{
    private int numFuncionario;
    private boolean administrador;
    private List<Papel> papeis;
}
