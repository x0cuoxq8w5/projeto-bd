package com.bd.ufrn.projeto.dtos;

import com.bd.ufrn.projeto.enums.TipoQuarto;

public record QuartoReservaRes(
        Long id,
        String numero,
        TipoQuarto tipoQuarto,
        Boolean ocupado,
        Boolean marcadoParaLimpeza
) {
}
