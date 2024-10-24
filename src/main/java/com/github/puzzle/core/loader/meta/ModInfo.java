package com.github.puzzle.core.loader.meta;

import com.github.puzzle.core.loader.meta.parser.ModJson;
import com.github.puzzle.core.loader.provider.mod.AdapterPathPair;
import com.github.puzzle.core.loader.provider.mod.ModContainer;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.hjson.JsonValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.jar.JarFile;

public class ModInfo {
    // Info
    public final String DisplayName;
    public final String ModID;
    public final Version ModVersion;
    public final String Description;
    public final ImmutableCollection<String> Authors;
    public final ImmutableMap<String, JsonValue> Metadata;

    // Entrypoints & Mixins
    public final ImmutableMap<String, ImmutableCollection<AdapterPathPair>> Entrypoints;
    public final ImmutableCollection<String> MixinConfigs;

    // Dependencies
    public final ImmutableMap<String, Version> RequiredDependencies;
    public final ImmutableMap<String, Version> OptionalDependencies;

    // Access Transformers
    public final String AccessTransformer;
    public final String AccessWidener;
    public final String AccessManipulator;

    // Extra Info
    public final ModJson JSON;
    private ModContainer Container;

    public ModInfo(@NotNull final ModJson JSON) {
        this.JSON = JSON;

        DisplayName = JSON.name();
        ModID = JSON.id();
        ModVersion = Version.parseVersion(JSON.version());
        Description = JSON.description();

        Authors = ImmutableList.copyOf(JSON.authors());

        if (JSON.meta() != null) {
            var MetadataBuilder = ImmutableMap.<String, JsonValue>builder();
            for (String key : JSON.meta().keySet()) {
                MetadataBuilder.put(key, JSON.meta().get(key));
            }
            Metadata = MetadataBuilder.build();
        } else Metadata = ImmutableMap.<String, JsonValue>builder().build();

        var EntrypointsBuilder = ImmutableMap.<String, ImmutableCollection<AdapterPathPair>>builder();
        for (String key : JSON.entrypoints().keySet()) {
            EntrypointsBuilder.put(key, ImmutableList.copyOf(JSON.entrypoints().get(key)));
        }
        Entrypoints = EntrypointsBuilder.build();

        if (JSON.mixins() != null)
            MixinConfigs = ImmutableList.copyOf(JSON.mixins());
        else MixinConfigs = ImmutableList.of();

        if (JSON.dependencies() != null) {
            var RequiredDependenciesBuilder = ImmutableMap.<String, Version>builder();
            for (String key : JSON.dependencies().keySet()) {
                RequiredDependenciesBuilder.put(key, Version.parseVersion(JSON.dependencies().get(key).replaceAll("[^\\d.]", "")));
            }
            RequiredDependencies = RequiredDependenciesBuilder.build();
        } else RequiredDependencies = ImmutableMap.of();

        if (JSON.optional() != null) {
            var OptionalDependenciesBuilder = ImmutableMap.<String, Version>builder();
            for (String key : JSON.optional().keySet()) {
                OptionalDependenciesBuilder.put(key, Version.parseVersion(JSON.optional().get(key).replaceAll("[^\\d.]", "")));
            }
            OptionalDependencies = OptionalDependenciesBuilder.build();
        } else OptionalDependencies = ImmutableMap.of();

        if (JSON.accessManipulator() != null) AccessManipulator = JSON.accessManipulator();
        else AccessManipulator = null;
        if (JSON.accessTransformer() != null) AccessTransformer = JSON.accessTransformer();
        else AccessTransformer = null;
        if (JSON.accessWidener() != null) AccessWidener = JSON.accessWidener();
        else AccessWidener = null;
    }

    @Contract("_ -> new")
    public static @NotNull ModInfo fromModJsonInfo(ModJson info) {
        return new ModInfo(info);
    }

    public ModContainer getOrCreateModContainer() {
        if (Container == null) Container = new ModContainer(this);
        return Container;
    }

    public ModContainer getOrCreateModContainer(JarFile file) {
        if (Container != null) Container = new ModContainer(this, file);
        return Container;
    }

    public static class Builder {

        private String id = null;
        private Version version = null;
        private String name = null;
        private String description = null;
        private List<String> authors = new ArrayList<>();
        private Map<String, Collection<AdapterPathPair>> entrypoints = new HashMap<>();
        private Map<String, JsonValue> meta = new HashMap<>();
        private List<String> mixins = new ArrayList<>();
        private Map<String, Version> dependencies = new HashMap<>();
        private Map<String, Version> optional = new HashMap<>();
        private String accessManipulator = null;
        private String accessTransformer = null;
        private String accessWidener = null;

        private Builder() {}

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setVersion(String version) {
            this.version = Version.parseVersion(version);
            return this;
        }

        public Builder setVersion(Version version) {
            this.version = version;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDesc(String desc) {
            this.description = desc;
            return this;
        }

        public Builder setAuthors(String[] authors) {
            this.authors = new ArrayList<>(List.of(authors));
            return this;
        }

        public Builder setAuthors(@NotNull Collection<String> authors) {
            this.authors = authors.stream().toList();
            return this;
        }

        public Builder addAuthors(String... names) {
            this.authors.addAll(List.of(names));
            return this;
        }

        public Builder addAuthor(String name) {
            this.authors.add(name);
            return this;
        }

        public Builder setEntrypoint(String name, Collection<AdapterPathPair> classes) {
            this.entrypoints.put(name, classes);
            return this;
        }

        public Builder addEntrypoint(String name, String adapter, String clazz) {
            if (this.entrypoints.get(name) != null) this.entrypoints.get(name).add(new AdapterPathPair(adapter, clazz));
            else {
                List<AdapterPathPair> classes = new ArrayList<>();
                classes.add(new AdapterPathPair(adapter, clazz));
                this.entrypoints.put(name, classes);
            }
            return this;
        }

        public Builder addEntrypoint(String name, String clazz) {
            if (this.entrypoints.get(name) != null) this.entrypoints.get(name).add(new AdapterPathPair("java", clazz));
            else {
                List<AdapterPathPair> classes = new ArrayList<>();
                classes.add(new AdapterPathPair("java", clazz));
                this.entrypoints.put(name, classes);
            }
            return this;
        }

        public Builder setEntrypoints(Map<String, Collection<AdapterPathPair>> entrypoints) {
            this.entrypoints = entrypoints;
            return this;
        }

        public Builder setMeta(Map<String, JsonValue> meta) {
            this.meta = meta;
            return this;
        }

        public Builder addMeta(String key, JsonValue value) {
            this.meta.put(key, value);
            return this;
        }

        public Builder setMixinConfigs(List<String> mixinConfigs) {
            this.mixins = mixinConfigs;
            return this;
        }

        public Builder addMixinConfig(String mixinConfigPath) {
            this.mixins.add(mixinConfigPath);
            return this;
        }

        public Builder addMixinConfigs(String... mixinConfigPaths) {
            this.mixins.addAll(List.of(mixinConfigPaths));
            return this;
        }

        public Builder setDependencies(Map<String, Version> dependencies) {
            this.dependencies = dependencies;
            return this;
        }

        public Builder addDependency(String name, Version version) {
            this.dependencies.put(name, version);
            return this;
        }

        public Builder setOptionalDependencies(Map<String, Version> dependencies) {
            this.optional = dependencies;
            return this;
        }

        public Builder addOptionalDependency(String name, Version version) {
            this.optional.put(name, version);
            return this;
        }

        public Builder setAccessTransformer(String transformerPath) {
            this.accessWidener = transformerPath;
            return this;
        }
        
        public Builder setAccessWidener(String transformerPath) {
            this.accessTransformer = transformerPath;
            return this;
        }
        
        public Builder setAccessManipulator(String transformerPath) {
            this.accessManipulator = transformerPath;
            return this;
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
            return new ModInfo(new ModJson(
                    0,
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

        @Contract(" -> new")
        public static @NotNull Builder New() {
            return new Builder();
        }

    }
}
