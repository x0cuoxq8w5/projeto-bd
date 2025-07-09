package com.bd.ufrn.projeto.services;

import com.bd.ufrn.projeto.dtos.EquipeLimpezaDTO;
import com.bd.ufrn.projeto.interfaces.CrudService;
import com.bd.ufrn.projeto.models.EquipeLimpeza;
import com.bd.ufrn.projeto.models.Funcionario;
import com.bd.ufrn.projeto.repositories.EquipeLimpezaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipeLimpezaService implements CrudService<EquipeLimpeza,EquipeLimpezaDTO, Integer> {
    @Autowired EquipeLimpezaRepository equipeLimpezaRepository;
    @Autowired
    private FuncionarioService funcionarioService;

    @Override
    public EquipeLimpeza get(Integer id) {
        return equipeLimpezaRepository.findById(id);
    }

    @Override
    public void create(EquipeLimpezaDTO equipeLimpezaDTO) {
        EquipeLimpeza equipeLimpeza = EquipeLimpeza.builder().build();
        equipeLimpezaRepository.save(equipeLimpeza);
    }

    @Override
    public void update(Integer id, EquipeLimpezaDTO equipeLimpezaDTO) {
        return;
    }

    @Override
    public void delete(Integer id) {
        EquipeLimpeza equipeLimpeza = get(id);
        equipeLimpezaRepository.delete(equipeLimpeza);
    }

    @Override
    public List<EquipeLimpeza> getAll() {
        return equipeLimpezaRepository.findAll();
    }

    public EquipeLimpeza getByFuncionario(String cpf) {
        Funcionario funcionario = funcionarioService.get(cpf);
        return equipeLimpezaRepository.findByFuncionario(funcionario);
    }

    public void addToEquipe(Integer id, String cpf) {
        EquipeLimpeza equipeLimpeza = get(id);
        Funcionario funcionario = funcionarioService.get(cpf);
        if (getByFuncionario(cpf) == null) {
            equipeLimpezaRepository.addToEquipe(equipeLimpeza, funcionario);
        } else { throw new RuntimeException("Funcionario ja esta em outra equipe!");}
    }

    public void removeFromEquipe(Integer id, String cpf) {
        EquipeLimpeza equipeLimpeza = get(id);
        Funcionario funcionario = funcionarioService.get(cpf);
        if (getByFuncionario(cpf) == equipeLimpeza) {
            equipeLimpezaRepository.removeFromEquipe(equipeLimpeza, funcionario);
        } else throw new RuntimeException("Funcionario nao esta nessa equipe!");
    }
}
