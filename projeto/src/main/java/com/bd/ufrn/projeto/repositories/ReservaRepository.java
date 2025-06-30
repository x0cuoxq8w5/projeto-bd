package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.interfaces.StrongEntity;
import com.bd.ufrn.projeto.models.Hospede;
import com.bd.ufrn.projeto.models.Reserva;

import java.util.List;

public class ReservaRepository extends AbstractRepository<Reserva> implements StrongEntity<Reserva,Integer> {
    //SELECT * FROM (reserva JOIN hospede ON cpf) WHERE cpf = <cpf>
    //Talvez receber o CPF direto?
    public List<Reserva> findByCpf(Integer cpf) {
        return null;
    }

    //SELECT * FROM reserva WHERE data_entrada <> null AND data_fim < LocalDateTime.now()
    public List<Reserva> findOngoing() {
        return null;
    }

    //SELECT * FROM reserva WHERE id_checkin = <id>
    public Reserva findByCheckinId(Integer id) {
        return null;
    }

    //SELECT * FROM (reserva NATURAL JOIN quarto) WHERE numero = <numQuarto>
    public List<Reserva> findByNumQuarto(Integer numQuarto) {
        return null;
    }
}
