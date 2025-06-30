package com.bd.ufrn.projeto.repositories;

import com.bd.ufrn.projeto.interfaces.StrongEntity;

import java.util.List;

public abstract class AbstractRepository<Type> {

    //INSERT INTO <TYPENAME> VALUES ()
    //checagem de null pros notnull, insere com null pros outros
    abstract public void save(Type entity) {
    }

    //DELETE FROM <TYPENAME> WHERE <id> = id
    abstract public void delete(Type entity) {}
    //SELECT * FROM <TYPENAME>
    abstract public List<Type> findAll() {
        return null;
    }
}
