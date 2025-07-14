package com.bd.ufrn.projeto.controllers;

import com.bd.ufrn.projeto.dtos.BreadcrumbItem;
import com.bd.ufrn.projeto.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/estoque")
public class EstoquePageController {

    private final ProdutoService produtoService;

    @Autowired
    public EstoquePageController(ProdutoService produtoService) {
        this.produtoService = produtoService;
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
}
