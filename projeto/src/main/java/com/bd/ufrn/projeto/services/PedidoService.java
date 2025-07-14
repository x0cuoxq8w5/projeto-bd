package com.bd.ufrn.projeto.services;

import com.bd.ufrn.projeto.dtos.ItemPedidoDTO;
import com.bd.ufrn.projeto.dtos.PedidoDTO;
import com.bd.ufrn.projeto.interfaces.CrudService;
import com.bd.ufrn.projeto.models.Pedido;
import com.bd.ufrn.projeto.models.Produto;

import com.bd.ufrn.projeto.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bd.ufrn.projeto.dtos.ItemPedido;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService implements CrudService<Pedido, PedidoDTO,Integer> {
    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private QuartoService quartoService;
    @Autowired private ProdutoService produtoService;

    @Override
    public Pedido get(Integer id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public void create(PedidoDTO pedidoDTO) {
        Pedido pedido = new Pedido();
        pedido.setDataPedido(pedidoDTO.dataPedido() != null ? pedidoDTO.dataPedido() : LocalDateTime.now());
        pedido.setDataEntrega(pedidoDTO.dataEntrega());
        pedido.setQuarto(quartoService.get(pedidoDTO.numeroQuarto()));

        List<ItemPedido> itens = pedidoDTO.itens().stream().map(itemDTO -> {
            Produto produto = produtoService.get(itemDTO.getProdutoId());
            
            return ItemPedido.builder()
                    .produto(produto)
                    .quantidade(itemDTO.getQuantidade())
                    .preco(produto.getPrecoAtual()) // O preço do item no momento do pedido é o preço atual do produto
                    .build();
        }).toList();

        pedido.setItemPedidos(itens);

        pedidoRepository.save(pedido);
    }

    @Override
    public void update(Integer id, PedidoDTO pedidoDTO) {
        Pedido pedido = get(id);
        if (pedidoDTO.dataEntrega() != null) pedido.setDataEntrega(pedidoDTO.dataEntrega());
        if (pedidoDTO.dataPedido() != null) pedido.setDataPedido(pedidoDTO.dataPedido());
        if (pedidoDTO.numeroQuarto() != null) pedido.setQuarto(quartoService.get(pedidoDTO.numeroQuarto()));

        if (pedidoDTO.itens() != null && !pedidoDTO.itens().isEmpty()) {
            List<ItemPedido> itens = pedidoDTO.itens().stream()
                    .filter(java.util.Objects::nonNull)
                    .map(itemDTO -> {
                Produto produto = produtoService.get(itemDTO.getProdutoId());
                return ItemPedido.builder()
                        .produto(produto)
                        .quantidade(itemDTO.getQuantidade())
                        .preco(produto.getPrecoAtual())
                        .build();
            }).toList();
            pedido.setItemPedidos(itens);
        }

        pedidoRepository.save(pedido);
    }

    @Override
    public void delete(Integer id) {
        Pedido pedido = get(id);
        pedidoRepository.delete(pedido);
    }

    @Override
    public List<Pedido> getAll() {
        return pedidoRepository.findAll();
    }

    public List<PedidoDTO> getAllAsDto() {
        return pedidoRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public PedidoDTO getByIdAsDto(Integer id) {
        Pedido pedido = get(id);
        if (pedido == null) {
            return null; 
        }
        return convertToDto(pedido);
    }

    private PedidoDTO convertToDto(Pedido pedido) {
        List<ItemPedidoDTO> itemDTOs = pedido.getItemPedidos() != null ? pedido.getItemPedidos().stream().map(item -> {
            ItemPedidoDTO itemDto = new ItemPedidoDTO();
            itemDto.setProdutoId(item.getProduto().getId());
            itemDto.setProdutoNome(item.getProduto().getNome());
            itemDto.setQuantidade(item.getQuantidade());
            itemDto.setPreco(item.getPreco());
            return itemDto;
        }).toList() : new java.util.ArrayList<>();

        Double custoTotal = itemDTOs.stream()
                .mapToDouble(item -> item.getPreco() * item.getQuantidade())
                .sum();

        return new PedidoDTO(
                pedido.getId(),
                pedido.getDataPedido(),
                pedido.getDataEntrega(),
                pedido.getQuarto().getNumero(),
                itemDTOs,
                custoTotal
        );
    }
}
