package com.github.puzzle.core.loader.meta;

import com.github.puzzle.core.loader.meta.parser.mod.ModJsonV1;
import com.github.puzzle.core.loader.provider.mod.AdapterPathPair;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.ImmutablePair;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import org.hjson.JsonValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ModInfoV1Builder extends ModInfo.Builder {

    protected String id = null;
    protected Version version = null;
    protected String name = null;
    protected String description = null;
    protected List<String> authors = new ArrayList<>();
    protected Map<String, Collection<AdapterPathPair>> entrypoints = new HashMap<>();
    protected Map<String, JsonValue> meta = new HashMap<>();
    protected List<Pair<EnvType, String>> mixins = new ArrayList<>();

    protected Map<String, Pair<String, Boolean>> depends = new HashMap<>();
    protected final List<String> accessTransformers = new ArrayList<>();

    protected ModInfoV1Builder() {
    }

    public ModInfo.Builder setId(String id) {
        this.id = id;
        return this;
    }

    public ModInfo.Builder setVersion(String version) {
        this.version = Version.parseVersion(version);
        return this;
    }

    public ModInfo.Builder setVersion(Version version) {
        this.version = version;
        return this;
    }

    public ModInfo.Builder setName(String name) {
        this.name = name;
        return this;
    }

    public ModInfo.Builder setDesc(String desc) {
        this.description = desc;
        return this;
    }

    public ModInfo.Builder setAuthors(String[] authors) {
        this.authors = new ArrayList<>(List.of(authors));
        return this;
    }

    public ModInfo.Builder setAuthors(@NotNull Collection<String> authors) {
        this.authors = authors.stream().toList();
        return this;
    }

    public ModInfo.Builder addAuthors(String... names) {
        this.authors.addAll(List.of(names));
        return this;
    }

    public ModInfo.Builder addAuthor(String name) {
        this.authors.add(name);
        return this;
    }

    public ModInfo.Builder setEntrypoint(String name, Collection<AdapterPathPair> classes) {
        this.entrypoints.put(name, classes);
        return this;
    }

    public ModInfo.Builder addEntrypoint(String name, String adapter, String clazz) {
        if (this.entrypoints.get(name) != null) this.entrypoints.get(name).add(new AdapterPathPair(adapter, clazz));
        else {
            List<AdapterPathPair> classes = new ArrayList<>();
            classes.add(new AdapterPathPair(adapter, clazz));
            this.entrypoints.put(name, classes);
        }
        return this;
    }

    public ModInfo.Builder addEntrypoint(String name, String clazz) {
        if (this.entrypoints.get(name) != null) this.entrypoints.get(name).add(new AdapterPathPair("java", clazz));
        else {
            List<AdapterPathPair> classes = new ArrayList<>();
            classes.add(new AdapterPathPair("java", clazz));
            this.entrypoints.put(name, classes);
        }
        return this;
    }

    public ModInfo.Builder setEntrypoints(Map<String, Collection<AdapterPathPair>> entrypoints) {
        this.entrypoints = entrypoints;
        return this;
    }

    public ModInfo.Builder setMeta(Map<String, JsonValue> meta) {
        this.meta = meta;
        return this;
    }

    public ModInfo.Builder addMeta(String key, JsonValue value) {
        this.meta.put(key, value);
        return this;
    }

    public ModInfo.Builder setSidedMixinConfigs(List<Pair<EnvType, String>> mixinConfigs) {
        this.mixins = mixinConfigs;
        return this;
    }

    public ModInfo.Builder addSidedMixinConfig(EnvType side, String mixinConfigPath) {
        this.mixins.add(new ImmutablePair<>(side, mixinConfigPath));
        return this;
    }

    public ModInfo.Builder addSidedMixinConfigs(EnvType side, String... mixinConfigPaths) {
        for (String mixin : mixinConfigPaths) {
            this.mixins.add(new ImmutablePair<>(side, mixin));
        }
        return this;
    }

    public ModInfo.Builder setDependenciesV2(Map<String, Pair<String, Boolean>> dependencies) {
        this.depends = dependencies;
        return this;
    }

    public ModInfo.Builder addOptionalDependency(String name, String version) {
        this.depends.put(name, new ImmutablePair<>(version, false));
        return this;
    }

    public ModInfo.Builder addDependency(String name, String version) {
        this.depends.put(name, new ImmutablePair<>(version, true));
        return this;
    }

    public ModInfo.Builder addDependency(String name, String version, Boolean isRequired) {
        this.depends.put(name, new ImmutablePair<>(version, isRequired));
        return this;
    }

    public ModInfo.Builder addAccessManipulator(String transformerPath) {
        accessTransformers.add(transformerPath);
        return this;
    }

    protected String makeId() {
        return id == null ?
                makeName().replaceAll(" ", "-").toLowerCase(Locale.ROOT) :
                id;
    }

    protected String makeName() {
        return name == null ? "exampleMod" : name;
    }

    public ModInfo build() {
        return new ModInfo(new ModJsonV1(
                makeName(),
                makeId(),
                version != null ? version.toString() : "1.0.0",
                description,
                authors.toArray(new String[0]),
                mixins.toArray(new Pair[0]),
                accessTransformers.toArray(new String[0]),
                meta,
                entrypoints,
                depends
        ));
    }

    @Contract(" -> new")
    public static @NotNull ModInfoV1Builder New() {
        return new ModInfoV1Builder();
    }

}
