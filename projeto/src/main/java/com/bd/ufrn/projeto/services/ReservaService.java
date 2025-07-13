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

    public List<ReservaListDto> getAllAsDto() {
        return reservaRepository.findAll().stream().map(ReservaListDto::new).collect(Collectors.toList());
    }

    public List<Reserva> getByCpf(String cpf) {
        return reservaRepository.findByCpf(cpf);
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

//    public ReservaDTO formReqToDTO(ReservaFormReq reservaFormReq) {
//        ReservaDTO reservaDTO = new ReservaDTO(
//                null,
//                reservaFormReq.getCpf(),
//                LocalDateTime.now(),
//                reservaFormReq.getDataFinal(),
//                LocalDateTime.now(),
//                reservaFormReq.getQuartoId(),
//                reservaFormReq.getNome(),
//                reservaFormReq.getTipoQuarto()
//        );
//
//        return reservaDTO;
//    }
}
