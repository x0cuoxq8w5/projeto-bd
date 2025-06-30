package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.interfaces.StrongEntity;
import com.bd.ufrn.projeto.models.Hospede;

import java.util.List;

public class HospedeRepository extends AbstractRepository<Hospede> implements StrongEntity<Hospede,Integer> {
    @Override
    public Hospede findById(Integer integer) {
        return null;
    }

    @Override
    public void save(Hospede entity) {

    }

    @Override
    public void delete(Hospede entity) {

    }

    @Override
    public List<Hospede> findAll() {
        return List.of();
    }
}
