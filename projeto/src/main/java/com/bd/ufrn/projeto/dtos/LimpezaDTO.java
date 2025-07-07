package com.bd.ufrn.projeto.dtos;

import java.time.LocalDateTime;

public record LimpezaDTO(
        Integer id,
        Integer idEquipe,
        Integer numQuarto,
        LocalDateTime data
        ) {
}
