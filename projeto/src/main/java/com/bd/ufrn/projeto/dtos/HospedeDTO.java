package com.bd.ufrn.projeto.dtos;

import java.time.LocalDateTime;

public record HospedeDTO(
        String cpf,
        LocalDateTime dataNascimento,
        String nome,
        Boolean desativado) {
}
