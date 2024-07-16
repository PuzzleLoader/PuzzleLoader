package dev.crmodders.puzzle.core;

import dev.crmodders.puzzle.annotations.Stable;

import java.util.Objects;

/**
 * Stores information about Registered Objects
 * contains a namespace and name
 * namespaces are usually the modid
 * @author Mr-Zombii
 */
@Stable
public class Identifier {

    public static Identifier of(String namespace, String name) {
        return new Identifier(namespace, name);
    }

    public static Identifier fromString(String id) {
        int index = id.indexOf(':');
        if(index == -1) return of("base", id);
        if(index != id.lastIndexOf(':')) throw new IllegalArgumentException("Malformed Identifier String: \"" + id + "\"");
        return of(id.substring(0, index), id.substring(index + 1));
    }

    public String namespace;
    public String name;

    public Identifier() {}

    public Identifier(String namespace, String name) {
        this.namespace = namespace;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier that = (Identifier) o;
        return Objects.equals(namespace, that.namespace) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, name);
    }

    @Override
    public String toString() {
        return namespace + ":" + name;
    }

}