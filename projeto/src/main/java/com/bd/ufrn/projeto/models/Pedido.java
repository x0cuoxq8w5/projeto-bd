package com.bd.ufrn.projeto.models;

import com.bd.ufrn.projeto.dtos.ItemPedidoDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class Pedido {
    private Integer id;
    private LocalDateTime dataPedido;
    private LocalDateTime dataEntrega;
    private Quarto quarto;
    private List<ItemPedidoDTO> itemPedidoDTOS;

    public double getCustoTotal() {
        if (itemPedidoDTOS == null) {
            return 0.0;
        }
        return itemPedidoDTOS.stream()
                .mapToDouble(item -> item.getPreco() * item.getQuantidade())
                .sum();
    }
}
