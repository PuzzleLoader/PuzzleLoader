package io.github.puzzle.cosmic.impl.util;

import io.github.puzzle.cosmic.api.util.IPuzzleIdentifier;

public class Identifier implements IPuzzleIdentifier {

    private String namespace;
    private String name;

    public Identifier() {}

    public Identifier(String namespace, String name) {
        this.namespace = namespace;
        this.name = name;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public String getName() {
        return name;
    }

    public static Identifier of(String id) {
        int index = id.indexOf(58);
        if (index != id.lastIndexOf(58)) {
            throw new IllegalArgumentException("Malformed Identifier String: \"" + id + "\"");
        } else {
            return index == -1 ? of("base", id) : of(id.substring(0, index), id.substring(index + 1));
        }
    }

    public static Identifier of(String namespace, String name) {
        return new Identifier(namespace, name);
    }
}
