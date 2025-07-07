package com.bd.ufrn.projeto.dtos;

import java.time.LocalDateTime;

public record ReservaDTO(
        Integer id,
        String cpfCliente,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        LocalDateTime dataEntrada,
        LocalDateTime dataSaida,
        Integer numQuarto
) {
}
