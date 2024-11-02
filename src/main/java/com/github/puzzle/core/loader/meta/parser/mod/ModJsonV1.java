package com.github.puzzle.core.loader.meta.parser.mod;

import com.github.puzzle.core.loader.launch.Piece;
import com.github.puzzle.core.loader.meta.EnvType;
import com.github.puzzle.core.loader.meta.parser.ModJson;
import com.github.puzzle.core.loader.provider.lang.ILangProvider;
import com.github.puzzle.core.loader.provider.lang.impl.LangProviderWrapper;
import com.github.puzzle.core.loader.provider.mod.AdapterPathPair;
import com.github.puzzle.core.loader.util.Reflection;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.ImmutablePair;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import org.hjson.JsonArray;
import org.hjson.JsonObject;
import org.hjson.JsonValue;

import java.util.*;

public class ModJsonV1 extends ModJson {

    private String name = "";
    private String id = "";

    String version = "";
    String description = "";

    String[] authors = new String[0];
    Map<String, JsonValue> meta = new HashMap<>();

    Map<String, Collection<AdapterPathPair>> entrypoints = new HashMap<>();
    Map<String, Pair<String, Boolean>> dependencies = new HashMap<>();

    String[] accessTransformers = new String[0];
    Pair<EnvType, String>[] mixins = new Pair[0];

    ModJsonV1() {}

    public ModJsonV1(
            String name,
            String id,
            String version,
            String description,
            String[] authors,
            Pair<EnvType, String>[] mixins,
            String[] accessTransformers,
            Map<String, JsonValue> meta,
            Map<String, Collection<AdapterPathPair>> entrypoints,
            Map<String, Pair<String, Boolean>> dependencies
    ) {
        this.id = id;
        this.version = version;
        this.name = name;
        this.description = description;
        this.authors = authors;
        this.entrypoints = entrypoints;
        this.meta = meta;
        this.mixins = mixins;
        this.accessTransformers = accessTransformers;
        this.dependencies = dependencies;
    }

    public static ModJson fromString(String string) {
        ModJsonV1 info = new ModJsonV1();
        JsonObject obj = (JsonObject) JsonValue.readHjson(string);

        List<String> names = obj.names();
        info.name = obj.getString("name", "No-Named-Nancy");
        info.id = obj.getString("id", null);
        info.version = obj.getString("version", "0.0.0");
        info.description = obj.getString("description", "Just a mod");

        if (names.contains("authors")) {
            JsonArray array = obj.get("authors").asArray();
            info.authors = new String[array.size()];
            for (int i = 0; i < info.authors.length; i++)
                info.authors[i] = array.get(i).asString();
        } else info.authors = new String[0];

        if (names.contains("accessTransformers")) {
            JsonArray array = obj.get("accessTransformers").asArray();
            info.accessTransformers = new String[array.size()];
            for (int i = 0; i < info.accessTransformers.length; i++)
                info.accessTransformers[i] = array.get(i).asString();
        } else info.accessTransformers = new String[0];

        if (names.contains("mixins")) {
            JsonArray array = obj.get("mixins").asArray();
            info.mixins = new Pair[array.size()];
            for (int i = 0; i < info.mixins.length; i++) {
                if (array.get(i).isString())
                    info.mixins[i] = new ImmutablePair<>(EnvType.UNKNOWN, array.get(i).asString());
                else {
                    JsonObject mixinObj = array.get(i).asObject();
                    info.mixins[i] = new ImmutablePair<>(
                            EnvType.valueOf(mixinObj.getString("environment", "unknown").toUpperCase()),
                            mixinObj.get("config").asString()
                    );
                }
            }
        } else info.mixins = new Pair[0];

        if (names.contains("depends")) {
            JsonObject depObj = obj.get("depends").asObject();
            info.dependencies = new HashMap<>();
            for (String name : depObj.names()) {
                JsonValue value = depObj.get(name);
                if (value.isString()) {
                    info.dependencies.put(name, new ImmutablePair<>(value.asString(), true));
                } else {
                    JsonObject object = value.asObject();
                    info.dependencies.put(name, new ImmutablePair<>(object.get("ver").asString(), object.get("isRequired").asBoolean()));
                }
            }
        } else info.dependencies = new HashMap<>();

        if (names.contains("meta")) {
            JsonObject objc = obj.get("meta").asObject();
            info.meta = new HashMap<>();
            for (String name : objc.names()) {
                info.meta.put(name, objc.get(name));
                if (objc.get("languageAdapters") != null) {
                    JsonObject adapters = objc.get("languageAdapters").asObject();
                    for (String id : adapters.names()) {
                        Class<?> clazz;
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

        return info;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String version() {
        return version;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public String[] authors() {
        return authors;
    }

    @Override
    public Map<String, Collection<AdapterPathPair>> entrypoints() {
        return entrypoints;
    }

    @Override
    public Map<String, JsonValue> meta() {
        return meta;
    }

    @Override
    public Pair<EnvType, String>[] mixins() {
        return mixins;
    }

    @Override
    public Map<String, Pair<String, Boolean>> dependencies() {
        return dependencies;
    }

    @Override
    public String[] accessTransformers() {
        return accessTransformers;
    }


}
