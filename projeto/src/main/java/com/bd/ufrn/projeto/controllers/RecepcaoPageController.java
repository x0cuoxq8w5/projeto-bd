package com.bd.ufrn.projeto.controllers;

import com.bd.ufrn.projeto.dtos.BreadcrumbItem;
import com.bd.ufrn.projeto.dtos.QuartoReservaRes;
import com.bd.ufrn.projeto.dtos.ReservaFormReq;
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
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("recepcao")
public class RecepcaoPageController {

    private final ReservaService reservaService;
    private final QuartoService quartoService;

    @Autowired
    public RecepcaoPageController(ReservaService reservaService, QuartoService quartoService) {
        this.reservaService = reservaService;
        this.quartoService = quartoService;
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


    @GetMapping("/reservas/quartos-disponiveis")
    @ResponseBody
    public List<QuartoReservaRes> getQuartosDisponiveis(@RequestParam("dataInicio") String dataInicio, @RequestParam("dataFim") String dataFim) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dataInicioDate = formatter.parse(dataInicio);
        Date dataFimDate = formatter.parse(dataFim);

        return reservaService.findQuartosDisponiveis(dataInicioDate, dataFimDate);
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