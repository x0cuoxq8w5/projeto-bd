package com.bd.ufrn.projeto.services;

import com.bd.ufrn.projeto.dtos.FuncionarioDTO;
import com.bd.ufrn.projeto.enums.Papel;
import com.bd.ufrn.projeto.interfaces.CrudService;
import com.bd.ufrn.projeto.models.Funcionario;
import com.bd.ufrn.projeto.repositories.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService implements CrudService<Funcionario, FuncionarioDTO, String> {
    @Autowired private FuncionarioRepository funcionarioRepository;


    @Override
    public Funcionario get(String id) {
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
    public void update(String id, FuncionarioDTO funcionarioDTO) {
        Funcionario funcionario = get(id);
        if(funcionarioDTO.nome() != null) funcionario.setNome(funcionarioDTO.nome());
        if(funcionarioDTO.numFuncionario() != null) funcionario.setNumFuncionario(funcionarioDTO.numFuncionario());
        if(funcionarioDTO.administrador() != null) funcionario.setAdministrador(funcionarioDTO.administrador());
        if(funcionarioDTO.papeis() != null) funcionario.setPapeis(funcionarioDTO.papeis());
        funcionarioRepository.save(funcionario);

    }

    @Override
    public void delete(String id) {
        Funcionario funcionario = get(id);
        funcionarioRepository.delete(funcionario);
    }

    @Override
    public List<Funcionario> getAll() {
        return funcionarioRepository.findAll();
    }

    public List<Funcionario> getByPapel(Papel papel) {
        return funcionarioRepository.findByPapel(papel);
    }
}
