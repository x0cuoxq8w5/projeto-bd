package com.bd.ufrn.projeto.services;

import com.bd.ufrn.projeto.dtos.FuncionarioDTO;
import com.bd.ufrn.projeto.interfaces.CrudService;
import com.bd.ufrn.projeto.models.Funcionario;
import com.bd.ufrn.projeto.repositories.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService implements CrudService<Funcionario, FuncionarioDTO, Integer> {
    @Autowired private FuncionarioRepository funcionarioRepository;


    @Override
    public Funcionario get(Integer id) {
        return funcionarioRepository.findById(id);
    }

    @Override
    public void create(FuncionarioDTO funcionarioDTO) {
        Funcionario funcionario = Funcionario.builder()
                .cpf(funcionarioDTO.cpf())
                .dataNascimento(funcionarioDTO.dataNascimento())
                .nome(funcionarioDTO.nome())
                .numFuncionario(funcionarioDTO.numFuncionario())
                .administrador(funcionarioDTO.administrador())
                .papeis(funcionarioDTO.papeis())
                .build();
        funcionarioRepository.save(funcionario);
    }

    @Override
    public void update(Integer id, FuncionarioDTO funcionarioDTO) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public List<Funcionario> getAll() {
        return List.of();
    }
}
