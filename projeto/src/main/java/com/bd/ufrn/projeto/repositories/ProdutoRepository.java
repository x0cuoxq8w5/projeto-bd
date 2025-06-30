package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.interfaces.StrongEntity;
import com.bd.ufrn.projeto.models.Produto;

import java.util.List;

public class ProdutoRepository extends AbstractRepository<Produto> implements StrongEntity<Produto,Integer> {
    //SELECT * FROM produto WHERE quantidade = 0
    public List<Produto> findUnstocked() {
        return null;
    }

    @Override
    public Produto findById(Integer integer) {
        return null;
    }

    @Override
    public void save(Produto entity) {

    }

    @Override
    public void delete(Produto entity) {

    }

    @Override
    public List<Produto> findAll() {
        return List.of();
    }
}
