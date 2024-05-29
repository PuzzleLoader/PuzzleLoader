package dev.crmodders.puzzle.game.registry.api;

import dev.crmodders.puzzle.game.registry.impl.FreezingRegistryImpl;
import dev.crmodders.puzzle.game.tags.Identifier;

public interface FreezingRegistry<T> extends AccessableRegistry<T> {

    void freeze();
    boolean isFrozen();
    T register(Identifier id, T object);

    AccessableRegistry<T> access() throws NotAccessibleException;

    static <T> FreezingRegistry<T> create() {
        return new FreezingRegistryImpl<>();
    }


}
