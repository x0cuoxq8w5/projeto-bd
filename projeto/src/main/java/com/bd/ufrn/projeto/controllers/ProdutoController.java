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
@RequestMapping("/produto")
public class ProdutoController implements GenericController<Produto, ProdutoDTO, Integer> {
    @Autowired ProdutoService produtoService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Produto> get(Integer id) {
        return null;
    }

    @PostMapping
    @Override
    public ResponseEntity<String> create(ProdutoDTO produtoDTO) {
        return null;
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> update(Integer id, ProdutoDTO produtoDTO) {
        return null;
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(Integer id) {
        return null;
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<Produto>> getAll() {
        return null;
    }
}
