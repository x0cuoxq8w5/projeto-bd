package com.bd.ufrn.projeto.controllers;

import com.bd.ufrn.projeto.dtos.FuncionarioDTO;
import com.bd.ufrn.projeto.enums.Papel;
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
    public ResponseEntity<Funcionario> get(@PathVariable String id) {
        Funcionario funcionario = funcionarioService.get(id);
        return ResponseEntity.ok().body(funcionario);
    }

    @PostMapping
    @Override
    public ResponseEntity<String> create(@RequestBody FuncionarioDTO funcionarioDTO) {
        funcionarioService.create(funcionarioDTO);
        return ResponseEntity.ok().body("Criado com sucesso.");
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable String id, @RequestBody FuncionarioDTO funcionarioDTO) {
        funcionarioService.update(id, funcionarioDTO);
        return ResponseEntity.ok().body("Atualizado com sucesso.");
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        funcionarioService.delete(id);
        return  ResponseEntity.ok().body("Deletado com sucesso.");
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<Funcionario>> getAll() {
        List<Funcionario> funcionario = funcionarioService.getAll();
        return ResponseEntity.ok().body(funcionario);
    }

    @GetMapping("/papel/{papel}")
    public ResponseEntity<List<Funcionario>> getByPapel(@PathVariable String papel) {
        List<Funcionario> funcionarios = funcionarioService.getByPapel(Papel.valueOf(papel));
        return ResponseEntity.ok().body(funcionarios);
    }
}
