package com.bd.ufrn.projeto.controllers;

import com.bd.ufrn.projeto.dtos.PedidoDTO;
import com.bd.ufrn.projeto.interfaces.GenericController;
import com.bd.ufrn.projeto.models.Pedido;
import com.bd.ufrn.projeto.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedido")
public class PedidoController implements GenericController<Pedido, PedidoDTO, Integer> {
    @Autowired PedidoService pedidoService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> get(Integer id) {
        return null;
    }

    @PostMapping
    @Override
    public ResponseEntity<String> create(PedidoDTO pedidoDTO) {
        return null;
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> update(Integer id, PedidoDTO pedidoDTO) {
        return null;
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(Integer id) {
        return null;
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<Pedido>> getAll() {
        return null;
    }
}
