package com.bd.ufrn.projeto.controllers;

import com.bd.ufrn.projeto.dtos.BreadcrumbItem;
import com.bd.ufrn.projeto.dtos.ProdutoDTO;
import com.bd.ufrn.projeto.models.Produto;
import com.bd.ufrn.projeto.services.ProdutoService;
import com.bd.ufrn.projeto.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/estoque")
public class EstoquePageController {

    private final ProdutoService produtoService;
    private final PedidoService pedidoService;

    @Autowired
    public EstoquePageController(ProdutoService produtoService, PedidoService pedidoService) {
        this.produtoService = produtoService;
        this.pedidoService = pedidoService;
    }

    @GetMapping("/produtos")
    public String listarProdutos(Model model) {
        List<BreadcrumbItem> breadcrumbs = new ArrayList<>();
        breadcrumbs.add(new BreadcrumbItem("Produtos", null, true));

        model.addAttribute("breadcrumbs", breadcrumbs);
        model.addAttribute("pageTitle", "Lista de Produtos");
        model.addAttribute("produtos", produtoService.getAllAsDto());

        return "estoque/lista-produtos";
    }

    @GetMapping("/produtos/novo")
    public String novoProdutoForm(Model model) {
        List<BreadcrumbItem> breadcrumbs = new ArrayList<>();
        breadcrumbs.add(new BreadcrumbItem("Produtos", "/estoque/produtos", false));
        breadcrumbs.add(new BreadcrumbItem("Novo", null, true));

        model.addAttribute("breadcrumbs", breadcrumbs);
        model.addAttribute("pageTitle", "Novo Produto");
        model.addAttribute("produto", new ProdutoDTO(null, "", 0.0, 0));

        return "estoque/novo-produto";
    }

    @PostMapping("/produtos/novo")
    public String salvarNovoProduto(@ModelAttribute("produto") ProdutoDTO produtoDTO) {
        produtoService.create(produtoDTO);
        return "redirect:/estoque/produtos";
    }

    @GetMapping("/produtos/editar/{id}")
    public String editarProdutoForm(@PathVariable Integer id, Model model) {
        Produto produto = produtoService.get(id);
        ProdutoDTO produtoDTO = new ProdutoDTO(produto.getId(), produto.getNome(), produto.getPrecoAtual(), produto.getQuantidade());

        List<BreadcrumbItem> breadcrumbs = new ArrayList<>();
        breadcrumbs.add(new BreadcrumbItem("Produtos", "/estoque/produtos", false));
        breadcrumbs.add(new BreadcrumbItem("Editar", null, true));

        model.addAttribute("breadcrumbs", breadcrumbs);
        model.addAttribute("pageTitle", "Editar Produto");
        model.addAttribute("produto", produtoDTO);

        return "estoque/editar-produto";
    }

    @PostMapping("/produtos/editar/{id}")
    public String salvarProdutoEditado(@PathVariable Integer id, @ModelAttribute("produto") ProdutoDTO produtoDTO) {
        produtoService.update(id, produtoDTO);
        return "redirect:/estoque/produtos";
    }

    @GetMapping("/pedidos")
    public String listarPedidos(Model model) {
        List<BreadcrumbItem> breadcrumbs = new ArrayList<>();
        breadcrumbs.add(new BreadcrumbItem("Pedidos", null, true));

        model.addAttribute("breadcrumbs", breadcrumbs);
        model.addAttribute("pageTitle", "Lista de Pedidos");
        model.addAttribute("pedidos", pedidoService.getAllAsDto());

        return "estoque/lista-pedidos";
    }

    @GetMapping("/pedidos/ver/{id}")
    public String verPedido(@PathVariable Integer id, Model model) {
        List<BreadcrumbItem> breadcrumbs = new ArrayList<>();
        breadcrumbs.add(new BreadcrumbItem("Pedidos", "/estoque/pedidos", false));
        breadcrumbs.add(new BreadcrumbItem("Ver", null, true));

        model.addAttribute("breadcrumbs", breadcrumbs);
        model.addAttribute("pageTitle", "Ver Pedido");
        model.addAttribute("pedido", pedidoService.getByIdAsDto(id));

        return "estoque/ver-pedido";
    }
}
