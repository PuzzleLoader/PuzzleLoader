package dev.crmodders.puzzle.core.mod;

import java.util.*;

public class ModInfoBuilder {

    private String id = null;
    private Version version = null;
    private String name = null;
    private String description = null;
    private List<String> authors = new ArrayList<>();
    private Map<String, Collection<String>> entrypoints = new HashMap<>();
    private Map<String, String> meta = new HashMap<>();
    private List<String> mixins = new ArrayList<>();
    private Map<String, Version> dependencies = new HashMap<>();
    private Map<String, Version> optional = new HashMap<>();
    private String accessManipulator = null;
    private String accessTransformer = null;
    private String accessWidener = null;

    private ModInfoBuilder() {}

    public ModInfoBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public ModInfoBuilder setVersion(String version) {
        this.version = Version.parseVersion(version);
        return this;
    }

    public ModInfoBuilder setVersion(Version version) {
        this.version = version;
        return this;
    }

    public ModInfoBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ModInfoBuilder setDesc(String desc) {
        this.description = desc;
        return this;
    }

    public ModInfoBuilder setAuthors(String[] authors) {
        this.authors = new ArrayList<>(List.of(authors));
        return this;
    }

    public ModInfoBuilder setAuthors(Collection<String> authors) {
        this.authors = authors.stream().toList();
        return this;
    }

    public ModInfoBuilder addAuthors(String... names) {
        this.authors.addAll(List.of(names));
        return this;
    }

    public ModInfoBuilder addAuthor(String name) {
        this.authors.add(name);
        return this;
    }

    public ModInfoBuilder setEntrypoint(String name, Collection<String> classes) {
        this.entrypoints.put(name, classes);
        return this;
    }

    public ModInfoBuilder addEntrypoint(String name, String clazz) {
        if (this.entrypoints.get(name) != null) this.entrypoints.get(name).add(clazz);
        else {
            List<String> classes = new ArrayList<>();
            classes.add(clazz);
            this.entrypoints.put(name, classes);
        }
        return this;
    }

    public ModInfoBuilder setEntrypoints(Map<String, Collection<String>> entrypoints) {
        this.entrypoints = entrypoints;
        return this;
    }

    public ModInfoBuilder setMeta(Map<String, String> meta) {
        this.meta = meta;
        return this;
    }

    public ModInfoBuilder addMeta(String key, String value) {
        this.meta.put(key, value);
        return this;
    }

    public ModInfoBuilder setMixinConfigs(List<String> mixinConfigs) {
        this.mixins = mixinConfigs;
        return this;
    }

    public ModInfoBuilder addMixinConfig(String mixinConfigPath) {
        this.mixins.add(mixinConfigPath);
        return this;
    }

    public ModInfoBuilder addMixinConfigs(String... mixinConfigPaths) {
        this.mixins.addAll(List.of(mixinConfigPaths));
        return this;
    }

    public ModInfoBuilder setDependencies(Map<String, Version> dependencies) {
        this.dependencies = dependencies;
        return this;
    }

    public ModInfoBuilder addDependency(String name, Version version) {
        this.dependencies.put(name, version);
        return this;
    }

    public ModInfoBuilder setOptionalDependencies(Map<String, Version> dependencies) {
        this.optional = dependencies;
        return this;
    }

    public ModInfoBuilder addOptionalDependency(String name, Version version) {
        this.optional.put(name, version);
        return this;
    }



    public ModInfoBuilder setAccessTransformerType(AccessTransformerType transformerType, String transformerPath) {
        switch (transformerType) {
            case ACCESS_MANIPULATOR -> this.accessManipulator = transformerPath;
            case ACCESS_TRANSFORMER -> this.accessWidener = transformerPath;
            case ACCESS_WIDENER -> this.accessTransformer = transformerPath;
        }
        return this;
    }

    private static Map<String, String> TransformDepencenciesMap(Map<String, Version> dependencies) {
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

    public ModJsonInfo build() {
        return new ModJsonInfo(
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
        );
    }

    public ModContainer buildToModContainer() {
        return new ModContainer(build());
    }

    public static ModInfoBuilder newBuilder() {
        return new ModInfoBuilder();
    }

}