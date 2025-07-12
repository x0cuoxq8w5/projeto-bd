package com.bd.ufrn.projeto.controllers;


import com.bd.ufrn.projeto.dtos.BreadcrumbItem;
import com.bd.ufrn.projeto.dtos.QuartoReservaRes;
import com.bd.ufrn.projeto.dtos.ReservaFormReq;
import com.bd.ufrn.projeto.enums.TipoQuarto;
import com.bd.ufrn.projeto.services.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("recepcao")
public class RecepcaoPageController {

    private final ReservaService reservaService;

    @Autowired
    public RecepcaoPageController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }


    @GetMapping("/reservas/nova")
    public String novaReserva(Model model) {
        // Create breadcrumb items
        List<BreadcrumbItem> breadcrumbs = new ArrayList<>();
        breadcrumbs.add(new BreadcrumbItem("Reservas", "/recepcao/reservas", false));
        breadcrumbs.add(new BreadcrumbItem("Nova Reserva", null, true));

        // Add model attributes
        model.addAttribute("breadcrumbs", breadcrumbs);
        model.addAttribute("pageTitle", "Nova Reserva");
        model.addAttribute("pageDescription", "Preencha os dados para criar uma nova reserva");
        model.addAttribute("reserva", new ReservaFormReq());

        // Temporary
        List<QuartoReservaRes> quartos = List.of(
                new QuartoReservaRes(1L, "1", TipoQuarto.SIMPLES, true, false),
                new QuartoReservaRes(2L, "2", TipoQuarto.SIMPLES, false, true),
                new QuartoReservaRes(3L, "3", TipoQuarto.SIMPLES, true, false),
                new QuartoReservaRes(4L, "4", TipoQuarto.SUITE, false, true),
                new QuartoReservaRes(5L, "5", TipoQuarto.SIMPLES, true, true),
                new QuartoReservaRes(6L, "6", TipoQuarto.SUITE, false, false),
                new QuartoReservaRes(7L, "7", TipoQuarto.SIMPLES, false, false),
                new QuartoReservaRes(8L, "8", TipoQuarto.SIMPLES, false, false),
                new QuartoReservaRes(9L, "9", TipoQuarto.SIMPLES, true, true),
                new QuartoReservaRes(10L, "10", TipoQuarto.COBERTURA, false, true),
                new QuartoReservaRes(11L, "11", TipoQuarto.COBERTURA, true, true),
                new QuartoReservaRes(12L, "12", TipoQuarto.COBERTURA, false, true),
                new QuartoReservaRes(13L, "13", TipoQuarto.COBERTURA, true, true),
                new QuartoReservaRes(14L, "14", TipoQuarto.COBERTURA, false, true),
                new QuartoReservaRes(15L, "15", TipoQuarto.COBERTURA, true, true),
                new QuartoReservaRes(16L, "16", TipoQuarto.COBERTURA, false, true),
                new QuartoReservaRes(17L, "17", TipoQuarto.COBERTURA, true, true),
                new QuartoReservaRes(18L, "18", TipoQuarto.COBERTURA, false, true),
                new QuartoReservaRes(19L, "19", TipoQuarto.COBERTURA, true, true),
                new QuartoReservaRes(20L, "20", TipoQuarto.COBERTURA, false, true),
                new QuartoReservaRes(21L, "21", TipoQuarto.COBERTURA, true, true)
        );
        model.addAttribute("quartos", quartos);

        return "reservas/nova-reserva";
    }


    @PostMapping("/reservas/nova")
    public String processarNovaReserva(@ModelAttribute("reserva") ReservaFormReq reserva) {
        System.out.println(reserva);



        return "redirect:/recepcao/reservas";
    }

    @GetMapping("/reservas")
    public String listarReservas(Model model) {

        return "reservas/lista-reservas";
    }
}