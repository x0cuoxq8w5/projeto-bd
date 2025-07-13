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
@RequestMapping("/api/pedido")
public class PedidoController implements GenericController<Pedido, PedidoDTO, Integer> {
    @Autowired PedidoService pedidoService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> get(@PathVariable Integer id) {
        Pedido pedido = pedidoService.get(id);
        return ResponseEntity.ok().body(pedido);
    }

    @PostMapping
    @Override
    public ResponseEntity<String> create(@RequestBody PedidoDTO pedidoDTO) {
        pedidoService.create(pedidoDTO);
        return ResponseEntity.ok().body("Criado com sucesso.");
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody PedidoDTO pedidoDTO) {
        pedidoService.update(id, pedidoDTO);
        return ResponseEntity.ok().body("Atualizado com sucesso.");
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        pedidoService.delete(id);
        return  ResponseEntity.ok().body("Deletado com sucesso.");
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<Pedido>> getAll() {
        List<Pedido> pedido = pedidoService.getAll();
        return ResponseEntity.ok().body(pedido);
    }
}
