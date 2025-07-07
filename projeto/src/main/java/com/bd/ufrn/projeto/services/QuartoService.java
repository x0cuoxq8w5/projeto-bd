package com.bd.ufrn.projeto.services;

import com.bd.ufrn.projeto.dtos.QuartoDTO;
import com.bd.ufrn.projeto.interfaces.CrudService;
import com.bd.ufrn.projeto.models.Quarto;
import com.bd.ufrn.projeto.repositories.QuartoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuartoService implements CrudService<Quarto, QuartoDTO,Integer> {
    @Autowired private QuartoRepository quartoRepository;

    @Override
    public Quarto get(Integer id) {
        return null;
    }

    @Override
    public void create(QuartoDTO quartoDTO) {

    }

    @Override
    public void update(Integer id, QuartoDTO quartoDTO) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public List<Quarto> getAll() {
        return List.of();
    }
}
