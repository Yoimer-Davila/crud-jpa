package com.crudjpa.service.impl;

import com.crudjpa.service.ICrudService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class CrudService<Entity, Id> implements ICrudService<Entity, Id> {

    private final JpaRepository<Entity, Id> repository;

    public CrudService(JpaRepository<Entity, Id> repository) { this.repository = repository; }

    @Override
    @Transactional
    public Entity save(Entity entity) throws RuntimeException { return this.repository.save(entity); }

    @Override
    @Transactional
    public void delete(Id id) throws RuntimeException { this.repository.deleteById(id); }

    @Override
    public List<Entity> getAll() throws RuntimeException { return this.repository.findAll(); }

    @Override
    public Optional<Entity> getById(Id id) throws RuntimeException { return this.repository.findById(id); }
}
