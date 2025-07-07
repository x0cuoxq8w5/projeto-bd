package com.bd.ufrn.projeto.dtos;

import com.bd.ufrn.projeto.enums.Papel;

import java.time.LocalDateTime;
import java.util.List;

public record FuncionarioDTO(
        String cpf,
        LocalDateTime dataNascimento,
        String nome,
        Integer numFuncionario,
        Boolean administrador,
        List<Papel> papeis
) {
}
