package com.github.puzzle.core.loader.meta;

import com.github.puzzle.core.loader.meta.parser.mod.ModJsonV0;
import com.github.puzzle.core.loader.provider.mod.AdapterPathPair;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import org.hjson.JsonValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Deprecated
public class ModInfoV0Builder extends ModInfo.Builder {

    protected String id = null;
    protected Version version = null;
    protected String name = null;
    protected String description = null;
    protected List<String> authors = new ArrayList<>();
    protected Map<String, Collection<AdapterPathPair>> entrypoints = new HashMap<>();
    protected Map<String, JsonValue> meta = new HashMap<>();
    protected List<String> mixins = new ArrayList<>();
    protected Map<String, Version> dependencies = new HashMap<>();
    protected Map<String, Version> optional = new HashMap<>();
    protected String accessManipulator = null;
    protected String accessTransformer = null;
    protected String accessWidener = null;

    protected ModInfoV0Builder() {
    }

    public ModInfoV0Builder setId(String id) {
        this.id = id;
        return this;
    }

    public ModInfoV0Builder setVersion(String version) {
        this.version = Version.parseVersion(version);
        return this;
    }

    public ModInfoV0Builder setVersion(Version version) {
        this.version = version;
        return this;
    }

    public ModInfoV0Builder setName(String name) {
        this.name = name;
        return this;
    }

    public ModInfoV0Builder setDesc(String desc) {
        this.description = desc;
        return this;
    }

    public ModInfoV0Builder setAuthors(String[] authors) {
        this.authors = new ArrayList<>(List.of(authors));
        return this;
    }

    public ModInfoV0Builder setAuthors(@NotNull Collection<String> authors) {
        this.authors = authors.stream().toList();
        return this;
    }

    public ModInfoV0Builder addAuthors(String... names) {
        this.authors.addAll(List.of(names));
        return this;
    }

    public ModInfoV0Builder addAuthor(String name) {
        this.authors.add(name);
        return this;
    }

    public ModInfoV0Builder setEntrypoint(String name, Collection<AdapterPathPair> classes) {
        this.entrypoints.put(name, classes);
        return this;
    }

    public ModInfoV0Builder addEntrypoint(String name, String adapter, String clazz) {
        if (this.entrypoints.get(name) != null) this.entrypoints.get(name).add(new AdapterPathPair(adapter, clazz));
        else {
            List<AdapterPathPair> classes = new ArrayList<>();
            classes.add(new AdapterPathPair(adapter, clazz));
            this.entrypoints.put(name, classes);
        }
        return this;
    }

    public ModInfoV0Builder addEntrypoint(String name, String clazz) {
        if (this.entrypoints.get(name) != null) this.entrypoints.get(name).add(new AdapterPathPair("java", clazz));
        else {
            List<AdapterPathPair> classes = new ArrayList<>();
            classes.add(new AdapterPathPair("java", clazz));
            this.entrypoints.put(name, classes);
        }
        return this;
    }

    public ModInfoV0Builder setEntrypoints(Map<String, Collection<AdapterPathPair>> entrypoints) {
        this.entrypoints = entrypoints;
        return this;
    }

    public ModInfoV0Builder setMeta(Map<String, JsonValue> meta) {
        this.meta = meta;
        return this;
    }

    public ModInfoV0Builder addMeta(String key, JsonValue value) {
        this.meta.put(key, value);
        return this;
    }

    @Deprecated
    public ModInfoV0Builder setMixinConfigs(List<String> mixinConfigs) {
        this.mixins = mixinConfigs;
        return this;
    }

    @Deprecated
    public ModInfoV0Builder addMixinConfig(String mixinConfigPath) {
        this.mixins.add(mixinConfigPath);
        return this;
    }

    @Deprecated
    public ModInfoV0Builder addMixinConfigs(String... mixinConfigPaths) {
        this.mixins.addAll(List.of(mixinConfigPaths));
        return this;
    }

    @Override
    public ModInfoV0Builder setSidedMixinConfigs(List<Pair<EnvType, String>> mixinConfigs) {
        throw new UnsupportedOperationException("Sided Mixin Configs are not supported on ModInfoV0");
    }

    @Override
    public ModInfoV0Builder addSidedMixinConfig(EnvType side, String mixinConfigPath) {
        throw new UnsupportedOperationException("Sided Mixin Configs are not supported on ModInfoV0");
    }

    @Override
    public ModInfoV0Builder addSidedMixinConfigs(EnvType side, String... mixinConfigPaths) {
        throw new UnsupportedOperationException("Sided Mixin Configs are not supported on ModInfoV0");
    }

    @Override
    public ModInfoV0Builder setDependenciesV2(Map<String, Pair<String, Boolean>> dependencies) {
        throw new UnsupportedOperationException("Better Dependencies are not supported on ModInfoV0");
    }

    @Deprecated
    public ModInfoV0Builder setDependencies(Map<String, Version> dependencies) {
        this.dependencies = dependencies;
        return this;
    }

    @Deprecated
    public ModInfoV0Builder addDependency(String name, Version version) {
        this.dependencies.put(name, version);
        return this;
    }

    @Override
    public ModInfoV0Builder addDependency(String name, String version) {
        this.dependencies.put(name, Version.parseVersion(version));
        return this;
    }

    @Override
    public ModInfoV0Builder addDependency(String name, String version, Boolean isRequired) {
        if (isRequired) addDependency(name, Version.parseVersion(version));
        else addOptionalDependency(name, Version.parseVersion(version));
        return this;
    }

    @Deprecated
    public ModInfoV0Builder setOptionalDependencies(Map<String, Version> dependencies) {
        this.optional = dependencies;
        return this;
    }

    @Deprecated
    public ModInfoV0Builder addOptionalDependency(String name, Version version) {
        this.optional.put(name, version);
        return this;
    }

    @Deprecated
    public ModInfoV0Builder setAccessTransformer(String transformerPath) {
        this.accessWidener = transformerPath;
        return this;
    }

    @Deprecated
    public ModInfoV0Builder setAccessWidener(String transformerPath) {
        this.accessTransformer = transformerPath;
        return this;
    }

    @Deprecated
    public ModInfoV0Builder setAccessManipulator(String transformerPath) {
        this.accessManipulator = transformerPath;
        return this;
    }

    @Override
    public ModInfoV0Builder addAccessManipulator(String transformerPath) {
        throw new UnsupportedOperationException("Lists of Access Transformers/Manipulators/Wideners are not supported on ModInfoV0");
    }

    private static @NotNull Map<String, String> TransformDepencenciesMap(@NotNull Map<String, Version> dependencies) {
        Map<String, String> map = new HashMap<>();
        for (String dep : dependencies.keySet()) {
            map.put(dep, dependencies.get(dep).toString());
        }
        return map;
    }

    private String makeId() {
        return id == null ?
                makeName().replaceAll(" ", "-").toLowerCase(Locale.ROOT) :
                id;
    }

    private String makeName() {
        return name == null ? "exampleMod" : name;
    }

    public ModInfo build() {
        return new ModInfo(new ModJsonV0(
                makeId(),
                version != null ? version.toString() : "1.0.0",
                makeName(),
                description,
                authors.toArray(new String[0]),
                entrypoints,
                meta,
                mixins.toArray(new String[0]),
                TransformDepencenciesMap(dependencies),
                TransformDepencenciesMap(optional),
                accessManipulator,
                accessTransformer,
                accessWidener
        ));
    }

}
