package com.crudjpa.service;

import java.util.List;
import java.util.Optional;

public interface ICrudService<Type, Id> {
    Type save(Type type) throws RuntimeException;
    void delete(Id id) throws RuntimeException;
    List<Type> getAll() throws RuntimeException;
    Optional<Type> getById(Id id) throws RuntimeException;
}