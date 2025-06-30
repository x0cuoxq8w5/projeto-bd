package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.interfaces.StrongEntity;
import com.bd.ufrn.projeto.models.Produto;

import java.util.List;

public class ProdutoRepository extends AbstractRepository<Produto> implements StrongEntity<Produto,Integer> {
    //SELECT * FROM produto WHERE quantidade = 0
    public List<Produto> findUnstocked() {
        return null;
    }
}
