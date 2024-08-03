package io.github.puzzle.core.registries;

import io.github.puzzle.core.Identifier;

import java.util.LinkedHashMap;

public class GenericRegistry<T> extends MapRegistry<T> {
    public GenericRegistry(Identifier identifier) {
        super(identifier, new LinkedHashMap<>(), true, true);
    }
}
