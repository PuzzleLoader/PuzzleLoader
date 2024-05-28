package dev.crmodders.puzzle.registry.api;

import net.appel.tags.Identifier;

public interface AccessableRegistry<T> {

    T get(Identifier identifier);
    boolean contains(Identifier identifier);
    Identifier[] getRegisteredNames();

}
