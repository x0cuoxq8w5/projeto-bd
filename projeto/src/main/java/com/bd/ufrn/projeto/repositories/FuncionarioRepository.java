package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.enums.Papel;
import com.bd.ufrn.projeto.interfaces.StrongEntity;
import com.bd.ufrn.projeto.models.Funcionario;
import com.bd.ufrn.projeto.models.User;

import java.util.List;

public class FuncionarioRepository extends AbstractRepository<Funcionario> implements StrongEntity<Funcionario,Integer> {
    //SELECT * FROM funcionario WHERE num_funcionario = <num>
    public Funcionario findByNum(Integer num) {
        return null;
    }
    //SELECT * FROM funcionario WHERE administrador = 1
    public List<Funcionario> findAdmins() {
        return null;
    }

    //SELECT * FROM (funcionario NATURAL JOIN papel) WHERE papel = <papel.name()>
    public List<Funcionario> findByPapel(Papel papel) {
        return null;
    }
}
