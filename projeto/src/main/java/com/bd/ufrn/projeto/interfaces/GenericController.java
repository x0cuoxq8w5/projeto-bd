package com.bd.ufrn.projeto.interfaces;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GenericController<Type,TypeDTO,IdType>{
    public ResponseEntity<Type> get(IdType id);
    public ResponseEntity<String> create(TypeDTO dto);
    public ResponseEntity<String> update(IdType id, TypeDTO dto);
    public ResponseEntity<String> delete(IdType id);
    public ResponseEntity<List<Type>> getAll();
}
