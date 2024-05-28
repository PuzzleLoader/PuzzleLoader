package dev.crmodders.puzzle.game.registry.api;

import dev.crmodders.puzzle.game.registry.impl.DynamicRegistryImpl;
import dev.crmodders.puzzle.game.tags.Identifier;

public interface DynamicRegistry<T> extends AccessableRegistry<T> {

    T register(Identifier id, T object);

    AccessableRegistry<T> access() throws NotAccessibleException;

    static <T> DynamicRegistry<T> create() {
        return new DynamicRegistryImpl<>();
    }

}
