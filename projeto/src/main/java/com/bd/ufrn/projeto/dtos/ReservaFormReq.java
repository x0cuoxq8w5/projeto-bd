package com.bd.ufrn.projeto.dtos;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

@Setter
@Getter
public class ReservaFormReq {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve estar no formato 000.000.000-00")
    private String cpf;

    @NotNull(message = "Data de nascimento é obrigatória")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    @NotBlank(message = "Tipo de quarto é obrigatório")
    private String tipoQuarto;

    @NotNull(message = "Data de início é obrigatória")
    @Future(message = "Data de início deve ser futura")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInicio;

    @NotNull(message = "Data final é obrigatória")
    @Future(message = "Data final deve ser futura")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFinal;

    @NotNull(message = "Quarto deve ser selecionado")
    private Long quartoId;

    public ReservaFormReq() {}

    public ReservaFormReq(String nome, String cpf, String tipoQuarto,
                          LocalDate dataInicio, LocalDate dataFinal, Long quartoId) {
        this.nome = nome;
        this.cpf = cpf;
        this.tipoQuarto = tipoQuarto;
        this.dataInicio = dataInicio;
        this.dataFinal = dataFinal;
        this.quartoId = quartoId;
    }

    @AssertTrue(message = "A data final deve ser posterior à data de início")
    public boolean isDataFinalValida() {
        if (dataInicio == null || dataFinal == null) {
            return true; // Deixa a validação @NotNull lidar com campos nulos
        }
        return dataFinal.isAfter(dataInicio);
    }

    @Override
    public String toString() {
        return "ReservaFormReq{" +
                "nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", tipoQuarto='" + tipoQuarto + '\'' +
                ", dataInicio=" + dataInicio +
                ", dataFinal=" + dataFinal +
                ", quartoId=" + quartoId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReservaFormReq that = (ReservaFormReq) o;

        if (nome != null ? !nome.equals(that.nome) : that.nome != null) return false;
        if (cpf != null ? !cpf.equals(that.cpf) : that.cpf != null) return false;
        if (tipoQuarto != null ? !tipoQuarto.equals(that.tipoQuarto) : that.tipoQuarto != null) return false;
        if (dataInicio != null ? !dataInicio.equals(that.dataInicio) : that.dataInicio != null) return false;
        if (dataFinal != null ? !dataFinal.equals(that.dataFinal) : that.dataFinal != null) return false;
        return quartoId != null ? quartoId.equals(that.quartoId) : that.quartoId == null;
    }

    @Override
    public int hashCode() {
        int result = nome != null ? nome.hashCode() : 0;
        result = 31 * result + (cpf != null ? cpf.hashCode() : 0);
        result = 31 * result + (tipoQuarto != null ? tipoQuarto.hashCode() : 0);
        result = 31 * result + (dataInicio != null ? dataInicio.hashCode() : 0);
        result = 31 * result + (dataFinal != null ? dataFinal.hashCode() : 0);
        result = 31 * result + (quartoId != null ? quartoId.hashCode() : 0);
        return result;
    }
}