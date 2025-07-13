package com.bd.ufrn.projeto.controllers;

import com.bd.ufrn.projeto.dtos.BreadcrumbItem;
import com.bd.ufrn.projeto.dtos.QuartoReservaRes;
import com.bd.ufrn.projeto.dtos.ReservaFormReq;


import com.bd.ufrn.projeto.services.HospedeService;
import com.bd.ufrn.projeto.services.QuartoService;
import com.bd.ufrn.projeto.services.ReservaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;




import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("recepcao")
public class RecepcaoPageController {

    private final ReservaService reservaService;
    private final QuartoService quartoService;
    private final HospedeService hospedeService;

    @Autowired
    public RecepcaoPageController(ReservaService reservaService, QuartoService quartoService, HospedeService hospedeService) {
        this.reservaService = reservaService;
        this.quartoService = quartoService;
        this.hospedeService = hospedeService;
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

        model.addAttribute("quartos", new ArrayList<QuartoReservaRes>());

        return "reservas/nova-reserva";
    }





    @PostMapping("/reservas/nova")
    public String processarNovaReserva(@ModelAttribute("reserva") ReservaFormReq reserva) {
        reservaService.processarNovaReserva(reserva);

        return "redirect:/recepcao/reservas";
    }

    @GetMapping("/reservas/registrar-entrada")
    public String showRegistrarEntradaPage(Model model) {
        List<BreadcrumbItem> breadcrumbs = new ArrayList<>();
        breadcrumbs.add(new BreadcrumbItem("Reservas", "/recepcao/reservas", false));
        breadcrumbs.add(new BreadcrumbItem("Registrar Entrada", "#", true));

        model.addAttribute("breadcrumbs", breadcrumbs);
        model.addAttribute("pageTitle", "Registrar Entrada de Hóspede");
        return "reservas/registrar-entrada";
    }



    @PostMapping("/reservas/registrar-entrada")
    public String registrarEntrada(@RequestParam("reservaId") Integer reservaId, Model model) {
        try {
            reservaService.registrarEntrada(reservaId);
            return "redirect:/recepcao/reservas";
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("pageTitle", "Registrar Entrada de Hóspede");
            List<BreadcrumbItem> breadcrumbs = new ArrayList<>();
            breadcrumbs.add(new BreadcrumbItem("Recepção", "/recepcao", false));
            breadcrumbs.add(new BreadcrumbItem("Registrar Entrada", null, true));
            model.addAttribute("breadcrumbs", breadcrumbs);
            return "reservas/registrar-entrada";
        }
    }



    @GetMapping("/reservas")
    public String listarReservas(Model model) {
        // Create breadcrumb items
        List<BreadcrumbItem> breadcrumbs = new ArrayList<>();
        breadcrumbs.add(new BreadcrumbItem("Reservas", null, true));

        model.addAttribute("breadcrumbs", breadcrumbs);
        model.addAttribute("pageTitle", "Lista de Reservas");
        model.addAttribute("reservas", reservaService.getAllAsDto());

        return "reservas/lista-reservas";
    }

    @GetMapping("/hospedes")
    public String listarHospedes(Model model) {
        // Create breadcrumb items
        List<BreadcrumbItem> breadcrumbs = new ArrayList<>();
        breadcrumbs.add(new BreadcrumbItem("Hóspedes", null, true));

        model.addAttribute("breadcrumbs", breadcrumbs);
        model.addAttribute("pageTitle", "Lista de Hóspedes");
        model.addAttribute("hospedes", hospedeService.getAllAsDto());

        return "hospedes/lista-hospedes";
    }

    @PostMapping("/hospedes/toggle-status")
    public String toggleHospedeStatus(@RequestParam("cpf") String cpf) {
        hospedeService.toggleDesativado(cpf);
        return "redirect:/recepcao/hospedes";
    }

    @GetMapping("/quartos")
    public String listarQuartos(Model model) {
        List<BreadcrumbItem> breadcrumbs = new ArrayList<>();
        breadcrumbs.add(new BreadcrumbItem("Quartos", null, true));

        model.addAttribute("breadcrumbs", breadcrumbs);
        model.addAttribute("pageTitle", "Status dos Quartos");
        model.addAttribute("pageDescription", "Visualize o status de todos os quartos do hotel");
        model.addAttribute("quartos", quartoService.getAll());

        return "quartos/lista-quartos";
    }
}