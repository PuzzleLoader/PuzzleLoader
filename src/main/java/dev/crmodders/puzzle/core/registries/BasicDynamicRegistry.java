package dev.crmodders.puzzle.core.registries;

import dev.crmodders.flux.tags.Identifier;

import java.util.*;

public class BasicDynamicRegistry<T> implements IRegistry.IDynamic<T> {

    Identifier registryId;
    Map<Identifier, T> dynamicStorage;
    List<RegistryObject<T>> registryObjects;

    public BasicDynamicRegistry(Identifier id) {
        registryId = id;
        dynamicStorage = new HashMap<>();
        registryObjects = new ArrayList<>();
    }

    @Override
    public T store(Identifier id, T object) {
        if (dynamicStorage.containsKey(id)) throw new RuntimeException("CANNOT HAVE 2+ OBJECTS WITH SAME ID \"" + id + "\" IN REGISTRY \"" + registryId + "\"");
        dynamicStorage.put(id, object);
        registryObjects.add(new RegistryObject<>(this, id));
        return object;
    }

    @Override
    public T get(Identifier id) {
        return dynamicStorage.get(id);
    }

    @Override
    public RegistryObject<T>[] getObjects() {
        return registryObjects.toArray(new RegistryObject[]{});
    }

    @Override
    public Iterator<T> getIterator() {
        return new RegistryIterator<>(this);
    }

    @Override
    public Identifier getId() {
        return registryId;
    }
}
