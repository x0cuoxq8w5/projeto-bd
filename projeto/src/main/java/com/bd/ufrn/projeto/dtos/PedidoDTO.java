package com.bd.ufrn.projeto.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoDTO(
        Integer id,
        LocalDateTime dataPedido,
        LocalDateTime dataEntrega,
        Integer numeroQuarto,
        List<ItemPedidoDTO> itens
) {
}
