package com.crudjpa.controller;

import com.crudjpa.mapping.ISimpleEntityMapper;
import com.crudjpa.service.ICrudService;

public abstract class SimpleCrudController<Entity, Id> extends CrudController<Entity, Id, Entity, Entity, Entity> {
    protected SimpleCrudController(ICrudService<Entity, Id> crudService, ISimpleEntityMapper<Entity> mapper) {
        super(crudService, mapper);
    }
}
