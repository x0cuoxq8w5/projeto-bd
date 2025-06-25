package com.bd.ufrn.projeto.repositories;

import java.util.List;

public abstract class AbstractRepository<Type,Id> {
    public void save(Type entity) {
    }
    public Type findById(Id id){
        return null;
    }
    public void delete(Type entity) {}
    public List<Type> findAll() {
        return null;
    }
}
