package dev.crmodders.puzzle.core.registries;

import dev.crmodders.puzzle.core.Identifier;

import java.util.LinkedHashMap;

public class GenericRegistry<T> extends MapRegistry<T> {
    public GenericRegistry(Identifier identifier) {
        super(identifier, new LinkedHashMap<>(), true, true);
    }
}
