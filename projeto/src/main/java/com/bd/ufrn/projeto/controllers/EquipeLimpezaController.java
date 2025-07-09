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
    public ResponseEntity<EquipeLimpeza> get(@PathVariable Integer id) {
        EquipeLimpeza equipeLimpeza = equipeLimpezaService.get(id);
        return ResponseEntity.ok().body(equipeLimpeza);
    }

    @PostMapping
    @Override
    public ResponseEntity<String> create(@RequestBody EquipeLimpezaDTO equipeLimpezaDTO) {
        equipeLimpezaService.create(equipeLimpezaDTO);
        return ResponseEntity.ok().body("Criado com sucesso.");
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody EquipeLimpezaDTO equipeLimpezaDTO) {
        equipeLimpezaService.update(id, equipeLimpezaDTO);
        return ResponseEntity.ok().body("Atualizado com sucesso.");
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        equipeLimpezaService.delete(id);
        return  ResponseEntity.ok().body("Deletado com sucesso.");
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<EquipeLimpeza>> getAll() {
        List<EquipeLimpeza> equipeLimpeza = equipeLimpezaService.getAll();
        return ResponseEntity.ok().body(equipeLimpeza);
    }
}
