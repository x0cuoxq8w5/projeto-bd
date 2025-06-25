package com.bd.ufrn.projeto.models;

import java.time.LocalDateTime;

public class Reserva {
    private int id;
    private int cpfCliente; //mesma coisa que pedido
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private int idCheckin;
    private int numQuarto;
}
