package com.bd.ufrn.projeto.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
public class Limpeza {
    private int idEquipe;
    private int numQuarto;
    private LocalDateTime data;
}
