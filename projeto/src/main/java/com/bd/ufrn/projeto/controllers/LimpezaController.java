package com.bd.ufrn.projeto.controllers;

import com.bd.ufrn.projeto.dtos.LimpezaDTO;
import com.bd.ufrn.projeto.interfaces.GenericController;
import com.bd.ufrn.projeto.models.Limpeza;
import com.bd.ufrn.projeto.services.LimpezaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LimpezaController implements GenericController<Limpeza,LimpezaDTO,Integer> {
    @Autowired LimpezaService limpezaService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Limpeza> get(@PathVariable Integer id) {
        Limpeza limpeza = limpezaService.get(id);
        return ResponseEntity.ok().body(limpeza);
    }

    @PostMapping
    @Override
    public ResponseEntity<String> create(@RequestBody LimpezaDTO limpezaDTO) {
        limpezaService.create(limpezaDTO);
        return ResponseEntity.ok().body("Criado com sucesso.");
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody LimpezaDTO limpezaDTO) {
        limpezaService.update(id, limpezaDTO);
        return ResponseEntity.ok().body("Atualizado com sucesso.");
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        limpezaService.delete(id);
        return  ResponseEntity.ok().body("Deletado com sucesso.");
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<Limpeza>> getAll() {
        List<Limpeza> limpeza = limpezaService.getAll();
        return ResponseEntity.ok().body(limpeza);
    }
}
