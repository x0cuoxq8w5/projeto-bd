package com.bd.ufrn.projeto.services;

import com.bd.ufrn.projeto.dtos.HospedeDTO;
import com.bd.ufrn.projeto.interfaces.CrudService;
import com.bd.ufrn.projeto.models.Hospede;
import com.bd.ufrn.projeto.repositories.HospedeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospedeService implements CrudService<Hospede,HospedeDTO,String> {
    @Autowired
    private HospedeRepository hospedeRepository;
    @Override
    public Hospede get(String id) {
        return hospedeRepository.findById(id);
    }

    @Override
    public void create(HospedeDTO hospedeDTO) {
        Hospede hospede = Hospede.builder()
                .cpf(hospedeDTO.cpf())
                .dataNascimento(hospedeDTO.dataNascimento())
                .nome(hospedeDTO.nome())
                .build();
        hospedeRepository.save(hospede);
    }

    @Override
    public void update(String id, HospedeDTO hospedeDTO) {
        Hospede hospede = get(id);
        if(hospedeDTO.nome() != null) hospede.setNome(hospedeDTO.nome());
        hospedeRepository.save(hospede);
    }

    @Override
    public void delete(String id) {
        Hospede hospede = get(id);
        hospedeRepository.delete(hospede);
    }

    @Override
    public List<Hospede> getAll() {
        return List.of();
    }
}
