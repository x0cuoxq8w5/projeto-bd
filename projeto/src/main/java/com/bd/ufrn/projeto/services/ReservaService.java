package com.bd.ufrn.projeto.services;

import com.bd.ufrn.projeto.dtos.ReservaDTO;
import com.bd.ufrn.projeto.dtos.ReservaFormReq;
import com.bd.ufrn.projeto.interfaces.CrudService;
import com.bd.ufrn.projeto.models.Reserva;
import com.bd.ufrn.projeto.repositories.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<Reserva> getByCpf(String cpf) {
        return reservaRepository.findByCpf(cpf);
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
