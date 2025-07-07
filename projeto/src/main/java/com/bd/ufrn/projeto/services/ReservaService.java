package com.bd.ufrn.projeto.services;

import com.bd.ufrn.projeto.dtos.ReservaDTO;
import com.bd.ufrn.projeto.interfaces.CrudService;
import com.bd.ufrn.projeto.models.Reserva;
import com.bd.ufrn.projeto.repositories.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaService implements CrudService<Reserva, ReservaDTO,Integer> {
    @Autowired private ReservaRepository reservaRepository;

    @Override
    public Reserva get(Integer id) {
        return null;
    }

    @Override
    public void create(ReservaDTO reservaDTO) {

    }

    @Override
    public void update(Integer id, ReservaDTO reservaDTO) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public List<Reserva> getAll() {
        return List.of();
    }
}
