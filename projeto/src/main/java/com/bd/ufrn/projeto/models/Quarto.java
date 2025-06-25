package com.bd.ufrn.projeto.models;

import com.bd.ufrn.projeto.enums.TipoQuarto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Quarto {
    private int numero;
    private boolean naoPerturbe;
    private boolean ocupado;
    private boolean marcadoPraLimpeza;
    private TipoQuarto tipo;

}
