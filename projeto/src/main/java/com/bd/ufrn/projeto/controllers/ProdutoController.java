package com.bd.ufrn.projeto.controllers;

import com.bd.ufrn.projeto.dtos.ProdutoDTO;
import com.bd.ufrn.projeto.interfaces.GenericController;
import com.bd.ufrn.projeto.models.Produto;
import com.bd.ufrn.projeto.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produto")
public class ProdutoController implements GenericController<Produto, ProdutoDTO, Integer> {
    @Autowired ProdutoService produtoService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Produto> get(@PathVariable Integer id) {
        Produto produto = produtoService.get(id);
        return ResponseEntity.ok().body(produto);
    }

    @PostMapping
    @Override
    public ResponseEntity<String> create(@RequestBody ProdutoDTO produtoDTO) {
        produtoService.create(produtoDTO);
        return ResponseEntity.ok().body("Criado com sucesso.");
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody ProdutoDTO produtoDTO) {
        produtoService.update(id, produtoDTO);
        return ResponseEntity.ok().body("Atualizado com sucesso.");
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        produtoService.delete(id);
        return  ResponseEntity.ok().body("Deletado com sucesso.");
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<Produto>> getAll() {
        List<Produto> produto = produtoService.getAll();
        return ResponseEntity.ok().body(produto);
    }
}
