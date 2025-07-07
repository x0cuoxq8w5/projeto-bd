package com.bd.ufrn.projeto.dtos;

import com.bd.ufrn.projeto.enums.TipoQuarto;

public record QuartoDTO(
        Integer numero,
        Boolean naoPerturbe,
        Boolean ocupado,
        Boolean marcadoPraLimpeza,
        TipoQuarto tipo
) {
}
