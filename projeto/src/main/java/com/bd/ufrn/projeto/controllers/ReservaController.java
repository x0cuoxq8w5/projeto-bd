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
@RequestMapping("reserva")
public class ReservaController implements GenericController<Reserva,ReservaDTO,Integer> {
    @Autowired
    ReservaService reservaService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> get(@PathVariable Integer id) {
        Reserva reserva = reservaService.get(id);
        return ResponseEntity.ok().body(reserva);
    }

    @PostMapping
    @Override
    public ResponseEntity<String> create(@RequestBody ReservaDTO reservaDTO) {
        reservaService.create(reservaDTO);
        return ResponseEntity.ok().body("Criado com sucesso.");
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody ReservaDTO reservaDTO) {
        reservaService.update(id, reservaDTO);
        return ResponseEntity.ok().body("Atualizado com sucesso.");
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        reservaService.delete(id);
        return  ResponseEntity.ok().body("Deletado com sucesso.");
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<Reserva>> getAll() {
        List<Reserva> reserva = reservaService.getAll();
        return ResponseEntity.ok().body(reserva);
    }
}
