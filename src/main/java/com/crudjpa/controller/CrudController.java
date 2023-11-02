package com.crudjpa.controller;

import com.crudjpa.enums.MapFrom;
import com.crudjpa.exception.CreateResourceValidationException;
import com.crudjpa.exception.ResourceNotFoundException;
import com.crudjpa.exception.UpdateResourceValidationException;
import com.crudjpa.mapping.IEntityMapper;
import com.crudjpa.service.ICrudService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class CrudController<Entity, Id, EntityResource, EntityCreate, EntityUpdate> {
    private final ICrudService<Entity, Id> crudService;
    protected final IEntityMapper<Entity, EntityResource, EntityCreate, EntityUpdate> mapper;
    protected String resourceName = "Resource";
    protected CrudController(ICrudService<Entity, Id> crudService, IEntityMapper<Entity, EntityResource, EntityCreate, EntityUpdate> mapper) {
        this.crudService = crudService;
        this.mapper = mapper;
    }

    protected abstract boolean isValidCreateResource(EntityCreate resource);
    protected abstract boolean isValidUpdateResource(EntityUpdate resource);


    protected EntityResource fromModelToResource(Entity entity, MapFrom from) { return mapper.fromModelToResource(entity); }
    protected EntityResource fromModelToResource(Entity entity) { return fromModelToResource(entity, MapFrom.ANY); }
    protected Entity fromCreateResourceToModel(EntityCreate create) { return mapper.fromCreateResourceToModel(create); }
    protected void fromUpdateResourceToModel(EntityUpdate update, Entity entity) { mapper.fromUpdateResourceToModel(update, entity); }
    protected String getErrorsFromResult(BindingResult result) {
        List<Map<String,String>> errors = result.getFieldErrors().stream()
                .map(err -> {
                    Map<String,String> error =  new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;

                }).toList();

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(errors);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    protected void throwResourceNotFoundException(String message) throws RuntimeException {
        throw new ResourceNotFoundException(message);
    }

    protected void throwResourceNotFoundException() throws RuntimeException {
        throwResourceNotFoundException("%s not found".formatted(resourceName));
    }

    protected List<EntityResource> getAllResources() {
        List<Entity> entities = this.crudService.getAll();
        return entities.stream().map(this::fromModelToResource).toList();
    }

    protected ResponseEntity<List<EntityResource>> getAll() {
        List<EntityResource> entities = getAllResources();
        if(entities.isEmpty())
            return new ResponseEntity<>(List.of(), HttpStatus.NO_CONTENT);
        return ResponseEntity.ok(entities);
    }

    protected EntityResource getResourceById(Id id, MapFrom from) {
        Entity entity = getEntityById(id);
        return this.fromModelToResource(entity, from);
    }

    protected EntityResource getResourceById(Id id) {
        return getResourceById(id, MapFrom.ANY);
    }


    protected Entity getEntityById(Id id) {
        Optional<Entity> resource = this.crudService.getById(id);

        if(resource.isEmpty())
            throwResourceNotFoundException();

        return resource.get();
    }

    protected ResponseEntity<EntityResource> getById(Id id) {
        EntityResource entity = getResourceById(id, MapFrom.GET);
        return ResponseEntity.ok(entity);
    }

    protected ResponseEntity<EntityResource> insert(EntityCreate resource){
        if(!isValidCreateResource(resource))
            throw new CreateResourceValidationException("Create resource for %s is not valid".formatted(resourceName));

        Entity entity = this.crudService.save(this.fromCreateResourceToModel(resource));
        return new ResponseEntity<>(this.fromModelToResource(entity, MapFrom.CREATE), HttpStatus.CREATED);
    }
    protected ResponseEntity<EntityResource> update(Id id, EntityUpdate resource) {
        if(!isValidUpdateResource(resource))
            throw new UpdateResourceValidationException("Update resource for %s is not valid".formatted(resourceName));

        Entity entity = getEntityById(id);
        fromUpdateResourceToModel(resource, entity);
        crudService.save(entity);
        return ResponseEntity.ok(this.fromModelToResource(entity, MapFrom.UPDATE));
    }
    protected ResponseEntity<EntityResource> delete(Id id) {
        Entity entity = getEntityById(id);
        this.crudService.delete(id);
        return ResponseEntity.ok(this.fromModelToResource(entity, MapFrom.DELETE));
    }
}
