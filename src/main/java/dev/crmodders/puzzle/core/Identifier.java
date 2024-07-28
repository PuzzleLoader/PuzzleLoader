package dev.crmodders.puzzle.core;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import dev.crmodders.puzzle.annotations.Stable;
import finalforeach.cosmicreach.chat.Chat;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Stores information about Registered Objects
 * contains a namespace and name
 * namespaces are usually the modid
 * @author Mr-Zombii
 */
@Stable
public class Identifier implements Json.Serializable {
    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Identifier of(String namespace, String name) {
        return new Identifier(namespace, name);
    }

    public static @NotNull Identifier fromString(@NotNull String id) {
        int index = id.indexOf(':');
        if(index == -1) return of("base", id);
        if(index != id.lastIndexOf(':')) throw new IllegalArgumentException("Malformed Identifier String: \"" + id + "\"");
        return of(id.substring(0, index), id.substring(index + 1));
    }

    private void internalFromString(@NotNull String id) {
        int index = id.indexOf(':');
        if(index == -1) {
            this.namespace = "base";
            this.name = id;
            return;
        }
        if(index != id.lastIndexOf(':')) throw new IllegalArgumentException("Malformed Identifier String: \"" + id + "\"");
        this.namespace = id.substring(0, index);
        this.name = id.substring(index + 1);
    }

    public String namespace;
    public String name;

    public Identifier() {
        internalFromString("base:nuhuh");
    }

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

    @Override
    public void write(Json json) {
        json.writeValue("pzID", toString());
    }

    @Override
    public void read(Json json, JsonValue jsonValue) {
        internalFromString(jsonValue.getString("pzID"));
    }
}