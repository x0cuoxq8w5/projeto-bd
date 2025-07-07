package com.bd.ufrn.projeto.interfaces;

import java.util.List;

public interface CrudService<Type, TypeDTO,IdType> {
    public Type get(IdType id);
    public void create(TypeDTO dto);
    public void update(IdType id, TypeDTO dto);
    public void delete(IdType id);
    public List<Type> getAll();

}
