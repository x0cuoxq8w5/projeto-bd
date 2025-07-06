package com.bd.ufrn.projeto.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
public class Limpeza {
    private Integer id;
    private EquipeLimpeza equipeLimpeza;
    private Quarto quarto;
    private LocalDateTime data;
}
