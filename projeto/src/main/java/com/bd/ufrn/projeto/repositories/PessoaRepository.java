package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.interfaces.StrongEntity;
import com.bd.ufrn.projeto.models.Pessoa;

import java.util.List;

public class PessoaRepository extends AbstractRepository<Pessoa> implements StrongEntity<Pessoa,Integer> {

    @Override
    public Pessoa findById(Integer integer) {
        return null;
    }

    @Override
    public void save(Pessoa entity) {

    }

    @Override
    public void delete(Pessoa entity) {

    }

    @Override
    public List<Pessoa> findAll() {
        return List.of();
    }
}
