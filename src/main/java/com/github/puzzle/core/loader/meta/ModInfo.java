package com.github.puzzle.core.loader.meta;

import com.github.puzzle.core.loader.meta.parser.ModJson;
import com.github.puzzle.core.loader.meta.parser.SideRequire;
import com.github.puzzle.core.loader.provider.mod.AdapterPathPair;
import com.github.puzzle.core.loader.provider.mod.ModContainer;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.ImmutablePair;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
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
    public final List<Pair<EnvType, String>> MixinConfigs;

    // Dependencies
    public final ImmutableMap<String, Pair<Version, Boolean>> Dependencies;

    // Access Transformers
    public final String[] AccessTransformers;

    // Extra Info
    public final ModJson JSON;
    private ModContainer Container;
    public final SideRequire allowedSides;

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
            MixinConfigs = Arrays.stream(JSON.mixins()).toList();
        else MixinConfigs = new ArrayList<>();

        if (JSON.dependencies() != null) {
            var DependenciesBuilder = ImmutableMap.<String, Pair<Version, Boolean>>builder();
            for (String key : JSON.dependencies().keySet()) {
                DependenciesBuilder.put(key, new ImmutablePair<>(
                        Version.parseVersion(JSON.dependencies().get(key).getLeft().replaceAll("[^\\d.]", "")),
                        JSON.dependencies().get(key).getRight()
                ));
            }
            Dependencies = DependenciesBuilder.build();
        } else Dependencies = ImmutableMap.of();

        if (JSON.accessTransformers() != null) AccessTransformers = JSON.accessTransformers();
        else AccessTransformers = new String[0];

        allowedSides = JSON.allowedSides();
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

    public static abstract class Builder {

        protected Builder() {}

        public abstract Builder setId(String id);

        public abstract Builder setVersion(String version);
        public abstract Builder setVersion(Version version);

        public abstract Builder setName(String name);
        public abstract Builder setDesc(String desc);

        public abstract Builder setAuthors(String[] authors);
        public abstract Builder setAuthors(@NotNull Collection<String> authors);
        public abstract Builder addAuthors(String... names);
        public abstract Builder addAuthor(String name);

        public abstract Builder setEntrypoint(String name, Collection<AdapterPathPair> classes);
        public abstract Builder addEntrypoint(String name, String adapter, String clazz);
        public abstract Builder addEntrypoint(String name, String clazz);
        public abstract Builder setEntrypoints(Map<String, Collection<AdapterPathPair>> entrypoints);

        public abstract Builder setMeta(Map<String, JsonValue> meta);
        public abstract Builder addMeta(String key, JsonValue value);

        public abstract Builder setSidedMixinConfigs(List<Pair<EnvType, String>> mixinConfigs);
        public abstract Builder addSidedMixinConfig(EnvType side, String mixinConfigPath);
        public abstract Builder addSidedMixinConfigs(EnvType side, String... mixinConfigPaths);

        public abstract Builder setDependenciesV2(Map<String, Pair<String, Boolean>> dependencies);
        public abstract Builder addDependency(String name, String version);
        public abstract Builder addDependency(String name, String version, Boolean isRequired);

        public abstract Builder addAccessManipulator(String manipulatorPath);

        public abstract ModInfo build();

        @Contract(" -> new")
        public static @NotNull Builder New() {
            return NewV2();
        }

        @Contract(" -> new")
        public static @NotNull ModInfoV2Builder NewV2() {
            return new ModInfoV2Builder();
        }

        @Contract(" -> new")
        public static @NotNull ModInfoV1Builder NewV1() {
            return new ModInfoV1Builder();
        }

        @Contract(" -> new")
        public static @NotNull ModInfoV0Builder NewV0() {
            return new ModInfoV0Builder();
        }

    }

}
