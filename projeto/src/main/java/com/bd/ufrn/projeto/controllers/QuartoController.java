package com.bd.ufrn.projeto.controllers;

import com.bd.ufrn.projeto.dtos.QuartoDTO;
import com.bd.ufrn.projeto.interfaces.GenericController;
import com.bd.ufrn.projeto.models.Quarto;
import com.bd.ufrn.projeto.services.QuartoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quarto")
public class QuartoController implements GenericController<Quarto,QuartoDTO,Integer> {
    @Autowired
    QuartoService quartoService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Quarto> get(Integer id) {
        return null;
    }

    @PostMapping
    @Override
    public ResponseEntity<String> create(QuartoDTO quartoDTO) {
        return null;
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> update(Integer id, QuartoDTO quartoDTO) {
        return null;
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(Integer id) {
        return null;
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<Quarto>> getAll() {
        return null;
    }
}
