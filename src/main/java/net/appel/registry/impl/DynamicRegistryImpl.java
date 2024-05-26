package net.appel.registry.impl;

import net.appel.registry.api.AccessableRegistry;
import net.appel.registry.api.DynamicRegistry;
import net.appel.registry.api.NotAccessibleException;
import net.appel.tags.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public class DynamicRegistryImpl<T> implements DynamicRegistry<T> {

    private Map<Identifier, T> objects;

    public DynamicRegistryImpl() {
        objects = new LinkedHashMap<>();
    }

    @Override
    public T get(Identifier identifier) {
        return objects.get(identifier);
    }

    @Override
    public boolean contains(Identifier identifier) {
        return objects.containsKey(identifier);
    }

    @Override
    public Identifier[] getRegisteredNames() {
        return objects.keySet().toArray(new Identifier[0]);
    }

    @Override
    public T register(Identifier id, T object) {
        objects.put(id, object);
        return object;
    }

    @Override
    public AccessableRegistry<T> access() throws NotAccessibleException {
        return this;
    }
}
