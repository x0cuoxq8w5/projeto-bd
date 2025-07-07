package com.bd.ufrn.projeto.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class Reserva {
    private Integer id;
    private Hospede hospede;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private Quarto quarto;
}
