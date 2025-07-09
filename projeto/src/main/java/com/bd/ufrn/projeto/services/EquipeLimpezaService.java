package com.bd.ufrn.projeto.services;

import com.bd.ufrn.projeto.dtos.EquipeLimpezaDTO;
import com.bd.ufrn.projeto.interfaces.CrudService;
import com.bd.ufrn.projeto.models.EquipeLimpeza;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipeLimpezaService implements CrudService<EquipeLimpeza,EquipeLimpezaDTO, Integer> {
    @Override
    public EquipeLimpeza get(Integer id) {
        return null;
    }

    @Override
    public void create(EquipeLimpezaDTO equipeLimpezaDTO) {

    }

    @Override
    public void update(Integer id, EquipeLimpezaDTO equipeLimpezaDTO) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public List<EquipeLimpeza> getAll() {
        return List.of();
    }
}
