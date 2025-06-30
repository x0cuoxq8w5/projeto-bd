package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.interfaces.StrongEntity;
import com.bd.ufrn.projeto.models.EquipeLimpeza;
import com.bd.ufrn.projeto.models.User;

import java.util.List;

public class EquipeLimpezaRepository extends AbstractRepository<EquipeLimpeza> implements StrongEntity<EquipeLimpeza,Integer> {
    @Override
    public EquipeLimpeza findById(Integer integer) {
        return null;
    }

    @Override
    public void save(EquipeLimpeza entity) {

    }

    @Override
    public void delete(EquipeLimpeza entity) {

    }

    @Override
    public List<EquipeLimpeza> findAll() {
        return List.of();
    }
}
