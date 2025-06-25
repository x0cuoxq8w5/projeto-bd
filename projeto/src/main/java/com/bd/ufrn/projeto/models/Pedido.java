package com.bd.ufrn.projeto.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
public class Pedido {
    private int id;
    private LocalDateTime dataPedido;
    private LocalDateTime dataEntrega;
    private int cpfCliente; //talvez trocar por classe cliente mesmo e usar um join_column
}
