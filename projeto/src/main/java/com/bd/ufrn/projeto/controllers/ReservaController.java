package com.bd.ufrn.projeto.controllers;

import com.bd.ufrn.projeto.dtos.ReservaDTO;
import com.bd.ufrn.projeto.interfaces.GenericController;
import com.bd.ufrn.projeto.models.Reserva;
import com.bd.ufrn.projeto.services.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class ReservaController implements GenericController<Reserva,ReservaDTO,Integer> {
    @Autowired
    ReservaService reservaService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> get(Integer id) {
        return null;
    }

    @PostMapping
    @Override
    public ResponseEntity<String> create(ReservaDTO reservaDTO) {
        return null;
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> update(Integer id, ReservaDTO reservaDTO) {
        return null;
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(Integer id) {
        return null;
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<Reserva>> getAll() {
        return null;
    }
}
