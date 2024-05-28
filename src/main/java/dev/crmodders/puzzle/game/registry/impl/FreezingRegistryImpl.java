package dev.crmodders.puzzle.game.registry.impl;

import dev.crmodders.puzzle.game.registry.api.AccessableRegistry;
import dev.crmodders.puzzle.game.registry.api.FreezingRegistry;
import dev.crmodders.puzzle.game.registry.api.NotAccessibleException;
import dev.crmodders.puzzle.game.tags.Identifier;

import java.util.HashMap;
import java.util.Map;

public class FreezingRegistryImpl<T> implements FreezingRegistry<T> {

    private boolean isFrozen;
    private final Map<Identifier, T> objects = new HashMap<>();

    public FreezingRegistryImpl() {}

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
    public void freeze() {
        isFrozen = true;
    }

    @Override
    public boolean isFrozen() {
        return isFrozen;
    }

    @Override
    public T register(Identifier id, T object) {
        if (isFrozen) throw new RuntimeException("CANNOT REGISTER AFTER REGISTRY IS FROZEN");
        objects.put(id, object);

        return object;
    }

    @Override
    public AccessableRegistry<T> access() throws NotAccessibleException {
        return this;
    }

}
