package com.bd.ufrn.projeto.models;

import java.time.LocalDateTime;

public class Pedido {
    private int id;
    private LocalDateTime dataPedido;
    private LocalDateTime dataEntrega;
    private int cpfCliente; //talvez trocar por classe cliente mesmo e usar um join_column
}
