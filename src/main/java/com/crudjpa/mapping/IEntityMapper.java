package com.crudjpa.mapping;

public interface IEntityMapper<Entity, EntityResource, CreateEntity, UpdateEntity> {
    Entity fromCreateResourceToModel(CreateEntity createEntity);
    void fromCreateResourceToModel(CreateEntity createEntity, Entity entity);
    EntityResource fromModelToResource(Entity entity);
    Entity fromUpdateResourceToModel(UpdateEntity updateEntity);
    void fromUpdateResourceToModel(UpdateEntity updateEntity, Entity entity);

}
