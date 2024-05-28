package dev.crmodders.puzzle.registry.api;

import dev.crmodders.puzzle.registry.impl.DynamicRegistryImpl;
import net.appel.tags.Identifier;

public interface DynamicRegistry<T> extends AccessableRegistry<T> {

    T register(Identifier id, T object);

    AccessableRegistry<T> access() throws NotAccessibleException;

    static <T> DynamicRegistry<T> create() {
        return new DynamicRegistryImpl<>();
    }

}
