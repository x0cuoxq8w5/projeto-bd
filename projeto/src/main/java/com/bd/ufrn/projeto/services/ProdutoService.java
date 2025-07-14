package com.bd.ufrn.projeto.services;

import com.bd.ufrn.projeto.dtos.ProdutoDto;

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
        return produtoRepository.findById(id);
    }

    @Override
    public void create(ProdutoDTO produtoDTO) {
        Produto produto = Produto.builder()
                .nome(produtoDTO.nome())
                .quantidade(produtoDTO.quantidade())
                .precoAtual(produtoDTO.precoAtual())
                .build();
        produtoRepository.save(produto);
    }

    @Override
    public void update(Integer id, ProdutoDTO produtoDTO) {
        Produto produto = get(id);
        if(produtoDTO.nome() != null) produto.setNome(produtoDTO.nome());
        produto.setQuantidade(produtoDTO.quantidade());
        if(produtoDTO.precoAtual() != null) produto.setPrecoAtual(produtoDTO.precoAtual());
        produtoRepository.save(produto);
    }

    @Override
    public void delete(Integer id) {
        Produto produto = get(id);
        produtoRepository.delete(produto);
    }

    @Override
    public List<Produto> getAll() {
        return produtoRepository.findAll();
    }

    public List<Produto> getByIds(List<Integer> ids) {
        return produtoRepository.findByIds(ids);
    }

    public List<ProdutoDto> getAllAsDto() {
        return produtoRepository.findAll().stream().map(ProdutoDto::new).collect(java.util.stream.Collectors.toList());
    }
}
