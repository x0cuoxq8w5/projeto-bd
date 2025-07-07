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
@RequestMapping
public class HospedeController implements GenericController<Hospede, HospedeDTO, String> {
    @Autowired
    HospedeService hospedeService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Hospede> get(String id) {
        return null;
    }

    @PostMapping
    @Override
    public ResponseEntity<String> create(HospedeDTO hospedeDTO) {
        return null;
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> update(String id, HospedeDTO hospedeDTO) {
        return null;
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(String id) {
        return null;
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<Hospede>> getAll() {
        return null;
    }
}
