package com.bd.ufrn.projeto.dtos;

import com.bd.ufrn.projeto.models.Hospede;
import java.time.LocalDateTime;

public record HospedeListDto(
    String cpf,
    String nome,
    LocalDateTime dataNascimento,
    Boolean desativado
) {
    public HospedeListDto(Hospede hospede) {
        this(
            hospede.getCpf(),
            hospede.getNome(),
            hospede.getDataNascimento(),
            hospede.isDesativado()
        );
    }
}
