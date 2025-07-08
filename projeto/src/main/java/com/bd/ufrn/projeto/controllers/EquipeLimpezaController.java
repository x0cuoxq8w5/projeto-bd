package com.bd.ufrn.projeto.controllers;

import com.bd.ufrn.projeto.dtos.EquipeLimpezaDTO;
import com.bd.ufrn.projeto.interfaces.GenericController;
import com.bd.ufrn.projeto.models.EquipeLimpeza;
import com.bd.ufrn.projeto.services.EquipeLimpezaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipe-limpeza")
public class EquipeLimpezaController implements GenericController<EquipeLimpeza, EquipeLimpezaDTO,Integer> {
    @Autowired EquipeLimpezaService equipeLimpezaService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<EquipeLimpeza> get(Integer id) {
        return null;
    }

    @PostMapping
    @Override
    public ResponseEntity<String> create(EquipeLimpezaDTO equipeLimpezaDTO) {
        return null;
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> update(Integer id, EquipeLimpezaDTO equipeLimpezaDTO) {
        return null;
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(Integer id) {
        return null;
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<EquipeLimpeza>> getAll() {
        return null;
    }
}
