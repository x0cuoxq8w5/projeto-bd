package com.bd.ufrn.projeto.repositories;

import java.util.List;

public abstract class AbstractRepository<Type,Id> {

    //INSERT INTO <TYPENAME> VALUES ()
    //checagem de null pros notnull, insere com null pros outros
    public void save(Type entity) {
    }
    //SELECT * FROM <TYPENAME> WHERE <id> = id
    public Type findById(Id id){
        return null;
    }
    //DELETE FROM <TYPENAME> WHERE <id> = id
    public void delete(Type entity) {}
    //SELECT * FROM <TYPENAME>
    public List<Type> findAll() {
        return null;
    }
}
