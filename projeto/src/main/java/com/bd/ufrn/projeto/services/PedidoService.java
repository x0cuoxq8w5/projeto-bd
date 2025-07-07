package com.bd.ufrn.projeto.services;

import com.bd.ufrn.projeto.dtos.PedidoDTO;
import com.bd.ufrn.projeto.interfaces.CrudService;
import com.bd.ufrn.projeto.models.Pedido;
import com.bd.ufrn.projeto.repositories.PedidoHasProdutoRepository;
import com.bd.ufrn.projeto.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService implements CrudService<Pedido, PedidoDTO,Integer> {
    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private PedidoHasProdutoRepository pedidoHasProdutoRepository;
    @Autowired private HospedeService hospedeService;

    @Override
    public Pedido get(Integer id) {
        return null;
    }

    @Override
    public void create(PedidoDTO pedidoDTO) {
        Pedido pedido = Pedido.builder()
                .dataPedido(LocalDateTime.now())
                .hospede(hospedeService.get(pedidoDTO.cpfHospede()))
                .build();

        //A func do repository disso DEVE criar o pedidoHasProduto atrelado a este pedido dentro de uma transação a fim
        //de impedir inconsistências no BD
    }

    @Override
    public void update(Integer id, PedidoDTO pedidoDTO) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public List<Pedido> getAll() {
        return List.of();
    }
}
