package com.bd.ufrn.projeto.services;

import com.bd.ufrn.projeto.dtos.ProdutoDTO;
import com.bd.ufrn.projeto.interfaces.CrudService;
import com.bd.ufrn.projeto.models.Produto;
import com.bd.ufrn.projeto.repositories.PedidoHasProdutoRepository;
import com.bd.ufrn.projeto.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService implements CrudService<Produto, ProdutoDTO,Integer> {
    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private PedidoHasProdutoRepository pedidoHasProdutoRepository;

    @Override
    public Produto get(Integer id) {
        return null;
    }

    @Override
    public void create(ProdutoDTO produtoDTO) {

    }

    @Override
    public void update(Integer id, ProdutoDTO produtoDTO) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public List<Produto> getAll() {
        return List.of();
    }
}
