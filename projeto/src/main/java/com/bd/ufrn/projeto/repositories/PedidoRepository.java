package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.interfaces.StrongEntity;
import com.bd.ufrn.projeto.models.Hospede;
import com.bd.ufrn.projeto.models.Pedido;
import com.bd.ufrn.projeto.models.Reserva;

import java.util.List;

public class PedidoRepository extends AbstractRepository<Pedido> implements StrongEntity<Pedido,Integer> {
    //SELECT * FROM (reserva JOIN hospede ON cpf) WHERE cpf = <cpf>
    //Tava cogitando criar uma interface mas são só duas classes então ninguém vai ligar
    //Talvez receber o CPF direto?
    public List<Reserva> findByCpf(Integer cpf) {
        return null;
    }

    @Override
    public Pedido findById(Integer integer) {
        return null;
    }

    @Override
    public void save(Pedido entity) {

    }

    @Override
    public void delete(Pedido entity) {

    }

    @Override
    public List<Pedido> findAll() {
        return List.of();
    }
}
