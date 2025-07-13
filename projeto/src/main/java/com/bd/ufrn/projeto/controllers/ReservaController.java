package com.bd.ufrn.projeto.controllers;

import com.bd.ufrn.projeto.dtos.QuartoReservaRes;
import com.bd.ufrn.projeto.dtos.ReservaDTO;
import com.bd.ufrn.projeto.dtos.ReservaListDto;
import com.bd.ufrn.projeto.interfaces.GenericController;
import com.bd.ufrn.projeto.models.Reserva;
import com.bd.ufrn.projeto.services.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController implements GenericController<Reserva, ReservaDTO, Integer> {
    @Autowired
    ReservaService reservaService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> get(@PathVariable Integer id) {
        return ResponseEntity.ok().body(reservaService.get(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<String> create(@RequestBody ReservaDTO reservaDTO) {
        reservaService.create(reservaDTO);
        return ResponseEntity.ok().body("Criado com sucesso.");
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody ReservaDTO reservaDTO) {
        reservaService.update(id, reservaDTO);
        return ResponseEntity.ok().body("Atualizado com sucesso.");
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        reservaService.delete(id);
        return ResponseEntity.ok().body("Deletado com sucesso.");
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<Reserva>> getAll() {
        return ResponseEntity.ok().body(reservaService.getAll());
    }

    @GetMapping("/quartos-disponiveis")
    public List<QuartoReservaRes> getQuartosDisponiveis(@RequestParam("dataInicio") String dataInicio, @RequestParam("dataFim") String dataFim) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dataInicioDate = formatter.parse(dataInicio);
        Date dataFimDate = formatter.parse(dataFim);

        return reservaService.findQuartosDisponiveis(dataInicioDate, dataFimDate);
    }

    @GetMapping("/buscar-por-cpf/{cpf}")
    public ResponseEntity<ReservaListDto> buscarReservaPorCpf(@PathVariable String cpf) {
        return reservaService.findActiveReservaByCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
