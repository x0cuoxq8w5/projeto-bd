package com.bd.ufrn.projeto.controllers;

import com.bd.ufrn.projeto.dtos.FuncionarioDTO;
import com.bd.ufrn.projeto.interfaces.GenericController;
import com.bd.ufrn.projeto.models.Funcionario;
import com.bd.ufrn.projeto.services.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController implements GenericController<Funcionario, FuncionarioDTO, String> {
    @Autowired
    FuncionarioService funcionarioService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> get(String id) {
        return null;
    }

    @PostMapping
    @Override
    public ResponseEntity<String> create(FuncionarioDTO funcionarioDTO) {
        return null;
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> update(String id, FuncionarioDTO funcionarioDTO) {
        return null;
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(String id) {
        return null;
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<Funcionario>> getAll() {
        return null;
    }
}
