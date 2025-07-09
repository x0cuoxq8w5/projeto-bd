package com.bd.ufrn.projeto.services;

import com.bd.ufrn.projeto.dtos.LimpezaDTO;
import com.bd.ufrn.projeto.interfaces.CrudService;
import com.bd.ufrn.projeto.models.Limpeza;
import com.bd.ufrn.projeto.models.Quarto;
import com.bd.ufrn.projeto.repositories.LimpezaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LimpezaService implements CrudService<Limpeza,LimpezaDTO,Integer> {
    @Autowired LimpezaRepository limpezaRepository;
    @Autowired EquipeLimpezaService equipeLimpezaService;
    @Autowired QuartoService quartoService;
    @Override
    public Limpeza get(Integer id) {
        return limpezaRepository.findById(id);
    }

    @Override
    public void create(LimpezaDTO limpezaDTO) {
        Quarto quarto = quartoService.get(limpezaDTO.numQuarto());
        if (quarto.isMarcadoParaLimpeza() && !quarto.isNaoPerturbe()) {
            Limpeza limpeza = Limpeza.builder()
                    .equipeLimpeza(equipeLimpezaService.get(limpezaDTO.idEquipe()))
                    .data(LocalDateTime.now())
                    .quarto(quarto)
                    .build();
            limpezaRepository.save(limpeza);
        } else {
            throw new RuntimeException("Quarto nao marcado pra limpeza ou nao perturbe ativo!");
        }
    }

    @Override
    public void update(Integer id, LimpezaDTO limpezaDTO) {
        Limpeza limpeza = get(id);
        if (limpezaDTO.numQuarto() != null) limpeza.setQuarto(quartoService.get(limpezaDTO.numQuarto()));
        if (limpezaDTO.idEquipe() != null) limpeza.setEquipeLimpeza(equipeLimpezaService.get(limpezaDTO.idEquipe()));
        limpezaRepository.save(limpeza);
    }

    @Override
    public void delete(Integer id) {
        limpezaRepository.delete(get(id));
    }

    @Override
    public List<Limpeza> getAll() {
        return limpezaRepository.findAll();
    }
}
