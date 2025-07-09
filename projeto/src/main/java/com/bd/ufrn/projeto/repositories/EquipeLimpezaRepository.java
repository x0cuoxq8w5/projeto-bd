package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.interfaces.StrongEntity;
import com.bd.ufrn.projeto.models.EquipeLimpeza;
import com.bd.ufrn.projeto.models.Funcionario;
import com.bd.ufrn.projeto.models.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
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

    //SELECT * FROM equipe_limpeza_has_funcionario NATURAL JOIN equipe_limpeza WHERE cpf = <funcionario.cpf>
    public EquipeLimpeza findByFuncionario(Funcionario funcionario) {
        return null;
    }

    //INSERT INTO equipe_limpeza_has_funcionario
    public void addToEquipe(EquipeLimpeza equipeLimpeza, Funcionario funcionario) {

    }

    //DELETE FROM equipe_limpeza_has_funcionario WHERE
    public void removeFromEquipe(EquipeLimpeza equipeLimpeza, Funcionario funcionario) {}
}
