package dev.crmodders.puzzle.game.registry.api;

import dev.crmodders.puzzle.game.tags.Identifier;

public interface AccessableRegistry<T> {

    T get(Identifier identifier);
    boolean contains(Identifier identifier);
    Identifier[] getRegisteredNames();

}
