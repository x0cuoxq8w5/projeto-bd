package com.bd.ufrn.projeto.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class Hospede extends Pessoa{
    protected boolean desativado;
}
