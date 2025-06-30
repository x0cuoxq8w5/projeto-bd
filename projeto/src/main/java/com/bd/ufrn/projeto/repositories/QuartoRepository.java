package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.enums.TipoQuarto;
import com.bd.ufrn.projeto.interfaces.StrongEntity;
import com.bd.ufrn.projeto.models.Quarto;

import java.util.List;

public class QuartoRepository extends AbstractRepository<Quarto> implements StrongEntity<Quarto,Integer> {
    //SELECT * FROM quarto WHERE ocupado = 1
    public List<Quarto> findByOcupado() {
        return null;
    }
    //SELECT * FROM quarto WHERE marcado_pra_limpeza = 1
    public List<Quarto> findByMarcado() {
        return null;
    }
    //SELECT * FROM quarto WHERE tipo LIKE <tipo.name()>
    public List<Quarto> findByTipo(TipoQuarto tipo) {
        return null;
    }

    @Override
    public Quarto findById(Integer integer) {
        return null;
    }

    @Override
    public void save(Quarto entity) {

    }

    @Override
    public void delete(Quarto entity) {

    }

    @Override
    public List<Quarto> findAll() {
        return List.of();
    }
}
