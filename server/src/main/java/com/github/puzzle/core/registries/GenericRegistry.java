package com.github.puzzle.core.registries;

import finalforeach.cosmicreach.util.Identifier;

import java.util.LinkedHashMap;

public class GenericRegistry<T> extends MapRegistry<T> {
    public GenericRegistry(Identifier identifier) {
        super(identifier, new LinkedHashMap<>(), true, true);
    }
}
