package com.github.puzzle.core.loader.meta.parser;

import com.github.puzzle.core.loader.launch.Piece;
import com.github.puzzle.core.loader.provider.lang.ILangProvider;
import com.github.puzzle.core.loader.provider.lang.impl.LangProviderWrapper;
import com.github.puzzle.core.loader.provider.mod.AdapterPathPair;
import com.github.puzzle.core.loader.util.Reflection;
import org.hjson.JsonArray;
import org.hjson.JsonObject;
import org.hjson.JsonValue;

import java.util.*;

public class ModJson {

    private int schemaVersion;
    private String id = null;
    String version = null;
    private String name = null;
    String description = null;
    String[] authors = null;
    Map<String, Collection<AdapterPathPair>> entrypoints = null;
    Map<String, JsonValue> meta = null;
    String[] mixins = null;
    Map<String, String> dependencies = null;
    Map<String, String> optional = null;
    String accessManipulator = null;
    String accessTransformer = null;
    String accessWidener = null;

    public ModJson(
            int schemaVersion,
            String id,
            String version,
            String name,
            String description,
            String[] authors,
            Map<String, Collection<AdapterPathPair>> entrypoints,
            Map<String, JsonValue> meta,
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

    ModJson() {}

    public static ModJson fromString(String string) {
        ModJson info = new ModJson();
        JsonObject obj = (JsonObject) JsonValue.readHjson(string);
        info.id = obj.getString("id", null);
        info.version = obj.getString("version", "0.0.0");
        info.name = obj.getString("name", null);
        info.description = obj.getString("description", null);
        if (obj.get("entrypoints") != null) {
            JsonObject entrypoints = obj.get("entrypoints").asObject();
            info.entrypoints = new HashMap<>();
            for (String name : entrypoints.names()) {
                List<AdapterPathPair> pairList = new ArrayList<>();
                for (JsonValue value : entrypoints.get(name).asArray()) {
                    if (value.isObject()) {
                        pairList.add(new AdapterPathPair(
                                value.asObject().getString("adapter", "java"),
                                value.asObject().get("value").asString()
                        ));
                    } else if (value.isString()) {
                        pairList.add(new AdapterPathPair("java", value.asString()));
                    }
                }
                info.entrypoints.put(name, pairList);
            }
        } else info.entrypoints = new HashMap<>();
        if (obj.get("authors") != null) {
            JsonArray values = obj.get("authors").asArray();
            info.authors = new String[values.size()];
            for (int i = 0; i < info.authors.length; i++) {
                info.authors[i] = values.get(i).asString();
            }
        } else info.authors = new String[0];
        if (obj.get("meta") != null) {
            JsonObject objc = obj.get("meta").asObject();
            info.meta = new HashMap<>();
            for (String name : objc.names()) {
                info.meta.put(name, objc.get(name));
                if (objc.get("languageAdapters") != null) {
                    JsonObject adapters = objc.get("languageAdapters").asObject();
                    for (String id : adapters.names()) {
                        Class<?> clazz = null;
                        try {
                            clazz = Class.forName(adapters.get(id).asString(), false, Piece.classLoader);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        Class<ILangProvider> adClass = (Class<ILangProvider>) clazz;
                        ILangProvider adapter = new LangProviderWrapper(Reflection.newInstance(adClass));
                        ILangProvider.PROVDERS.put(id, adapter);
                    }
                }
            }
        } else info.meta = new HashMap<>();
        if (obj.get("mixins") != null) {
            JsonArray values = obj.get("mixins").asArray();
            info.mixins = new String[values.size()];
            for (int i = 0; i < info.mixins.length; i++) {
                info.mixins[i] = values.get(i).asString();
            }
        } else info.mixins = new String[0];
        if (obj.get("dependencies") != null) {
            JsonObject objc = obj.get("dependencies").asObject();
            info.dependencies = new HashMap<>();
            for (String name : objc.names()) {
                info.dependencies.put(name, objc.get(name).asString());
            }
        } else info.dependencies = new HashMap<>();
        if (obj.get("optional") != null) {
            JsonObject objc = obj.get("optional").asObject();
            info.optional = new HashMap<>();
            for (String name : objc.names()) {
                info.optional.put(name, objc.get(name).asString());
            }
        } else info.optional = new HashMap<>();
        info.accessWidener = obj.getString("accessWidener", null);
        info.accessTransformer = obj.getString("accessTransformer", null);
        info.accessManipulator = obj.getString("accessManipulator", null);
        return info;
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

    public Map<String, Collection<AdapterPathPair>> entrypoints() {
        return entrypoints;
    }

    public Map<String, JsonValue> meta() {
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
