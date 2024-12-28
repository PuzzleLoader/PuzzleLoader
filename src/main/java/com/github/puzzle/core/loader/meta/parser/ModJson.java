package com.github.puzzle.core.loader.meta.parser;

import com.github.puzzle.core.loader.meta.EnvType;
import com.github.puzzle.core.loader.meta.parser.mod.ModJsonV0;
import com.github.puzzle.core.loader.meta.parser.mod.ModJsonV1;
import com.github.puzzle.core.loader.meta.parser.mod.ModJsonV2;
import com.github.puzzle.core.loader.provider.mod.AdapterPathPair;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import org.hjson.JsonObject;
import org.hjson.JsonValue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class ModJson {

    public static int latestRevision = 2;

    public static ModJson fromString(String string) {
        JsonObject obj = (JsonObject) JsonValue.readHjson(string);
        int version = obj.getInt("formatVersion", 0);

        ModJson json = switch (version) {
            case 0 -> ModJsonV0.fromString(string);
            case 1 -> ModJsonV1.fromString(string);
            case 2 -> ModJsonV2.fromString(string);
            default -> throw new RuntimeException("Invalid ModJson Version $" + version);
        };

        return convert(json, latestRevision);
    }

    public abstract int getRevision();

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

    public SideRequires sidedRequirements() {
        return SideRequires.BOTH_REQUIRED;
    }

    public static ModJson convert(ModJson old, int revision) {
        if (old.getRevision() == revision) return old;

        return switch (revision) {
            case 0 -> ModJsonV0.transform(old);
            case 1 -> ModJsonV1.transform(old);
            case 2 -> ModJsonV2.transform(old);
            default -> throw new IllegalStateException("Unexpected revision: " + revision);
        };
    }
}
