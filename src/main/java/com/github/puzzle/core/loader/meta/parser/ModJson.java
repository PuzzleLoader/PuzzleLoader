package com.github.puzzle.core.loader.meta.parser;

import com.github.puzzle.core.loader.meta.EnvType;
import com.github.puzzle.core.loader.meta.parser.mod.ModJsonV0;
import com.github.puzzle.core.loader.meta.parser.mod.ModJsonV1;
import com.github.puzzle.core.loader.provider.mod.AdapterPathPair;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import org.hjson.JsonObject;
import org.hjson.JsonValue;

import java.util.*;

public abstract class ModJson {

    public static ModJson fromString(String string) {
        JsonObject obj = (JsonObject) JsonValue.readHjson(string);
        int version = obj.getInt("formatVersion", 0);

        return switch (version) {
            case 0 -> ModJsonV0.fromString(string);
            case 1 -> ModJsonV1.fromString(string);
            default -> throw new RuntimeException("Invalid ModJson Version $" + version);
        };

    }

    public abstract String id();

    public abstract String version();

    public abstract String name();

    public abstract String description();

    public abstract String[] authors();

    public abstract Map<String, Collection<AdapterPathPair>> entrypoints();

    public abstract Map<String, JsonValue> meta();

    public abstract Pair<EnvType, String>[] mixins();

    public abstract Map<String, Pair<String, Boolean>> dependencies();

    public abstract String[] accessTransformers();
}
