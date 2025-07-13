package com.bd.ufrn.projeto.dtos;

import com.bd.ufrn.projeto.models.Reserva;

import java.time.LocalDateTime;

public record ReservaListDto(
        Integer id,
        String hospedeNome,
        Integer quartoNumero,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        LocalDateTime dataEntrada,
        LocalDateTime dataSaida
) {
    public ReservaListDto(Reserva reserva) {
        this(
                reserva.getId(),
                reserva.getHospede().getNome(),
                reserva.getQuarto().getNumero(),
                reserva.getDataInicio(),
                reserva.getDataFim(),
                reserva.getDataEntrada(),
                reserva.getDataSaida()
        );
    }
}
