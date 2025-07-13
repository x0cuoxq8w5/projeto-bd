package com.bd.ufrn.projeto.services;

import com.bd.ufrn.projeto.dtos.ReservaListDto;
import com.bd.ufrn.projeto.dtos.ReservaDTO;
import com.bd.ufrn.projeto.dtos.ReservaFormReq;
import com.bd.ufrn.projeto.dtos.HospedeDTO;
import com.bd.ufrn.projeto.models.Hospede;

import com.bd.ufrn.projeto.interfaces.CrudService;
import com.bd.ufrn.projeto.models.Reserva;
import com.bd.ufrn.projeto.repositories.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bd.ufrn.projeto.dtos.QuartoReservaRes;
import com.bd.ufrn.projeto.models.Quarto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservaService implements CrudService<Reserva, ReservaDTO,Integer> {
    @Autowired private ReservaRepository reservaRepository;
    @Autowired private HospedeService hospedeService;
    @Autowired private QuartoService quartoService;

    @Override
    public Reserva get(Integer id) {
        return reservaRepository.findById(id);
    }

    @Override
    public void create(ReservaDTO reservaDTO) {
        Reserva reserva = Reserva.builder()
                .dataInicio(LocalDateTime.now())
                .dataFim(reservaDTO.dataFim())
                .hospede(hospedeService.get(reservaDTO.cpfCliente()))
                .quarto(quartoService.get(reservaDTO.numQuarto()))
                .build();
        reservaRepository.save(reserva);
    }

    @Override
    public void update(Integer id, ReservaDTO reservaDTO) {
        Reserva reserva = reservaRepository.findById(id);
        if (reservaDTO.dataEntrada() != null) reserva.setDataEntrada(reservaDTO.dataEntrada());
        if (reservaDTO.dataSaida() != null) reserva.setDataEntrada(reservaDTO.dataSaida());
        reservaRepository.save(reserva);
    }

    @Override
    public void delete(Integer id) {
        Reserva reserva = get(id);
        reservaRepository.delete(reserva);
    }

    @Override
    public List<Reserva> getAll() {
        return reservaRepository.findAll();
    }

    public List<ReservaListDto> findAllReservas() {
        return reservaRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ReservaListDto convertToDto(Reserva reserva) {
        return new ReservaListDto(reserva);
    }

    public Optional<ReservaListDto> findActiveReservaByCpf(String cpf) {
        Hospede hospede = hospedeService.get(cpf);
        if (hospede == null) {
            return Optional.empty();
        }
        return reservaRepository.findActiveReservaByCpf(cpf).stream()
        .min(Comparator.comparing(Reserva::getDataInicio))
        .map(this::convertToDto);
    }

    public void registrarEntrada(Integer reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId);
        if (reserva == null) {
            throw new IllegalArgumentException("Reserva não encontrada");
        }
        if (reserva.getDataEntrada() != null) {
            throw new IllegalStateException("Check-in já foi realizado para esta reserva.");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(reserva.getDataInicio()) || now.isAfter(reserva.getDataFim())) {
            throw new IllegalStateException("Check-in só pode ser realizado entre a data de início e fim da reserva.");
        }

        reserva.setDataEntrada(now);
        reservaRepository.save(reserva);
    }

    public void registrarSaida(Integer reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId);
        if (reserva == null) {
            throw new IllegalArgumentException("Reserva não encontrada");
        }
        if (reserva.getDataEntrada() == null) {
            throw new IllegalStateException("Check-in ainda não foi realizado para esta reserva.");
        }
        if (reserva.getDataSaida() != null) {
            throw new IllegalStateException("Check-out já foi realizado para esta reserva.");
        }

        reserva.setDataSaida(LocalDateTime.now());
        reservaRepository.save(reserva);
    }

    public List<Reserva> getByCpf(String cpf) {
        return reservaRepository.findByCpf(cpf);
    }

    public List<ReservaListDto> getAllAsDto() {
        return reservaRepository.findAll().stream().map(ReservaListDto::new).collect(Collectors.toList());
    }

    public void processarNovaReserva(ReservaFormReq reservaFormReq) {
        Hospede hospede = hospedeService.get(reservaFormReq.getCpf());

        if (hospede == null) {
            HospedeDTO novoHospede = new HospedeDTO(
                    reservaFormReq.getCpf(),
                    reservaFormReq.getDataNascimento().atStartOfDay(),
                    reservaFormReq.getNome(),
                    false
            );
            hospedeService.create(novoHospede);
            hospede = hospedeService.get(reservaFormReq.getCpf());
        }

        Reserva reserva = Reserva.builder()
                .dataInicio(reservaFormReq.getDataInicio().atStartOfDay())
                .dataFim(reservaFormReq.getDataFinal().atStartOfDay())
                .hospede(hospede)
                .quarto(quartoService.get(reservaFormReq.getQuartoId().intValue()))
                .build();

        reservaRepository.save(reserva);
    }

    public List<QuartoReservaRes> findQuartosDisponiveis(Date dataInicio, Date dataFim) {
        LocalDateTime dataInicioDateTime = dataInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime dataFimDateTime = dataFim.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        List<Integer> quartosOcupadosIds = reservaRepository.findQuartosOcupados(dataInicioDateTime, dataFimDateTime);

        List<Quarto> quartosDisponiveis = quartoService.getAll().stream()
                .filter(quarto -> !quartosOcupadosIds.contains(quarto.getNumero()))
                .toList();

        return quartosDisponiveis.stream()
                .map(quarto -> new QuartoReservaRes((long) quarto.getNumero(), String.valueOf(quarto.getNumero()), quarto.getTipo(), false, false))
                .collect(Collectors.toList());
    }
}
