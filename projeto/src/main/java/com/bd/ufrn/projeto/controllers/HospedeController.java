package com.bd.ufrn.projeto.controllers;

import com.bd.ufrn.projeto.dtos.HospedeDTO;
import com.bd.ufrn.projeto.interfaces.GenericController;
import com.bd.ufrn.projeto.models.Hospede;
import com.bd.ufrn.projeto.services.HospedeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hospede")
public class HospedeController implements GenericController<Hospede, HospedeDTO, String> {
    @Autowired
    HospedeService hospedeService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Hospede> get(@PathVariable String id) {
        Hospede hospede = hospedeService.get(id);
        return ResponseEntity.ok().body(hospede);
    }

    @PostMapping
    @Override
    public ResponseEntity<String> create(@RequestBody HospedeDTO hospedeDTO) {
        hospedeService.create(hospedeDTO);
        return ResponseEntity.ok().body("Criado com sucesso.");
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable String id, @RequestBody HospedeDTO hospedeDTO) {
        hospedeService.update(id, hospedeDTO);
        return ResponseEntity.ok().body("Atualizado com sucesso.");
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        hospedeService.delete(id);
        return  ResponseEntity.ok().body("Deletado com sucesso.");
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<Hospede>> getAll() {
        List<Hospede> hospede = hospedeService.getAll();
        return ResponseEntity.ok().body(hospede);
    }
}
