package dev.crmodders.puzzle.loader.mod;

import java.util.Collection;
import java.util.Map;

public class ModJsonInfo {

    String id = null;
    String version = null;
    String name = null;
    String description = null;
    String[] authors = null;
    Map<String, Collection<String>> entrypoints = null;
    Map<String, Object> meta = null;
    String[] mixins = null;
    Map<String, String> dependencies = null;
    Map<String, String> optional = null;
    String accessManipulator = null;
    String accessTransformer = null;
    String accessWidener = null;

    public ModJsonInfo(
            String id,
            String version,
            String name,
            String description,
            String[] authors,
            Map<String, Collection<String>> entrypoints,
            Map<String, Object> meta,
            String[] mixins,
            Map<String, String> dependencies,
            Map<String, String> optional,
            String accessManipulator,
            String accessTransformer,
            String accessWidener
    ) {
        this.id = id;
        this.version = version;
        this.name = name;
        this.description = description;
        this.authors = authors;
        this.entrypoints = entrypoints;
        this.meta = meta;
        this.mixins = mixins;
        this.dependencies = dependencies;
        this.optional = optional;
        this.accessManipulator = accessManipulator;
        this.accessTransformer = accessTransformer;
        this.accessWidener = accessWidener;
    }

    public String id() {
        return id;
    }

    public String version() {
        return version;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public String[] authors() {
        return authors;
    }

    public Map<String, Collection<String>> entrypoints() {
        return entrypoints;
    }

    public Map<String, Object> meta() {
        return meta;
    }

    public String[] mixins() {
        return mixins;
    }

    public Map<String, String> dependencies() {
        return dependencies;
    }

    public Map<String, String> optional() {
        return optional;
    }

    public String accessManipulator() {
        return accessManipulator;
    }

    public String accessTransformer() {
        return accessTransformer;
    }

    public String accessWidener() {
        return accessWidener;
    }
}
