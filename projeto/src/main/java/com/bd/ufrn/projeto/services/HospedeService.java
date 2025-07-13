package com.bd.ufrn.projeto.services;

import com.bd.ufrn.projeto.dtos.HospedeListDto;
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
                .desativado(false)
                .build();
        hospedeRepository.save(hospede);
    }

    @Override
    public void update(String id, HospedeDTO hospedeDTO) {
        Hospede hospede = get(id);
        if(hospedeDTO.nome() != null) hospede.setNome(hospedeDTO.nome());
        if(hospedeDTO.desativado() != null) hospede.setDesativado(hospedeDTO.desativado());
        hospedeRepository.save(hospede);
    }

    @Override
    public void delete(String id) {
        Hospede hospede = get(id);
        hospedeRepository.delete(hospede);
    }

    @Override
    public List<Hospede> getAll() {
        return hospedeRepository.findAll();
    }

    public List<HospedeListDto> getAllAsDto() {
        return hospedeRepository.findAll().stream().map(HospedeListDto::new).collect(java.util.stream.Collectors.toList());
    }

    public void toggleDesativado(String cpf) {
        Hospede hospede = get(cpf);
        if (hospede != null) {
            hospede.setDesativado(!hospede.isDesativado());
            hospedeRepository.save(hospede);
        }
    }
}
