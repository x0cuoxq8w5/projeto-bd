package com.bd.ufrn.projeto.controllers;

import com.bd.ufrn.projeto.dtos.QuartoDTO;
import com.bd.ufrn.projeto.interfaces.GenericController;
import com.bd.ufrn.projeto.models.Quarto;
import com.bd.ufrn.projeto.services.QuartoService;
import org.apache.coyote.Response;
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
    public ResponseEntity<Quarto> get(@PathVariable Integer id) {
        Quarto quarto = quartoService.get(id);
        return ResponseEntity.ok().body(quarto);
    }

    @PostMapping
    @Override
    public ResponseEntity<String> create(@RequestBody QuartoDTO quartoDTO) {
        quartoService.create(quartoDTO);
        return ResponseEntity.ok().body("Criado com sucesso.");
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody QuartoDTO quartoDTO) {
        quartoService.update(id, quartoDTO);
        return ResponseEntity.ok().body("Atualizado com sucesso.");
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        quartoService.delete(id);
        return  ResponseEntity.ok().body("Deletado com sucesso.");
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<Quarto>> getAll() {
        List<Quarto> quarto = quartoService.getAll();
        return ResponseEntity.ok().body(quarto);
    }

    @GetMapping("/ocupado") ResponseEntity<List<Quarto>> getByOcupado() {
        List<Quarto> quarto = quartoService.getByOcupado();
        return ResponseEntity.ok().body(quarto);
    }

    @GetMapping("/marcado") ResponseEntity<List<Quarto>> getByMarcado() {
        List<Quarto> quartos = quartoService.getByMarcadoParaLimpeza();
        return ResponseEntity.ok().body(quartos);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Quarto>> getByTipo(@PathVariable String tipo) {
        List<Quarto> quartos = quartoService.findByTipo(tipo);
        return ResponseEntity.ok().body(quartos);
    }
}
