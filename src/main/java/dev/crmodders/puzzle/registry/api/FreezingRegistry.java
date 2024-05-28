package dev.crmodders.puzzle.registry.api;

import dev.crmodders.puzzle.registry.impl.FreezingRegistryImpl;
import net.appel.tags.Identifier;

public interface FreezingRegistry<T> extends AccessableRegistry<T> {

    void freeze();
    boolean isFrozen();
    T register(Identifier id, T object);

    AccessableRegistry<T> access() throws NotAccessibleException;

    static <T> FreezingRegistry<T> create() {
        return new FreezingRegistryImpl<>();
    }


}
