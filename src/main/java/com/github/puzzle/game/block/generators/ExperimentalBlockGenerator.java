package com.github.puzzle.game.block.generators;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.google.gson.Gson;
import finalforeach.cosmicreach.util.Identifier;
import org.hjson.JsonObject;
import org.hjson.Stringify;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ExperimentalBlockGenerator extends BlockGenerator {

    static final Json json = new Json();
    static final Gson gson = new Gson();

    public static class JsonBlock {
        public String stringId;
        public LinkedHashMap<String, String> defaultParams;
        public LinkedHashMap<String, BlockGenerator.State> blockStates;
        public String blockEntityId;
        public LinkedHashMap<String, ?> blockEntityParams;
        public LinkedHashMap<String, ?> defaultProperties;
    }

    public ExperimentalBlockGenerator(Identifier blockId) {
        super(blockId);

        this.blockId = blockId;
    }

    public Identifier blockId;
    public HashMap<String, String> defaultParams;
    public LinkedHashMap<String, BlockGenerator.State> blockStates;
    public String blockEntityId;
    public HashMap<String, ?> blockEntityParams;
    public HashMap<String, ?> defaultProperties;

    public static ExperimentalBlockGenerator fromJson(String json) {
        JsonBlock block = gson.fromJson(json, JsonBlock.class);
        ExperimentalBlockGenerator generator = new ExperimentalBlockGenerator(Identifier.of(block.stringId));

        generator.defaultParams = block.defaultParams;
        generator.blockStates = block.blockStates;
        generator.blockEntityId = block.blockEntityId;
        generator.blockEntityParams = block.blockEntityParams;
        generator.defaultProperties = block.defaultProperties;

        return generator;
    }

    @Override
    public String generateJson() {
        Gson gson = new Gson();
        json.setTypeName(null);
        json.setOutputType(JsonWriter.OutputType.json);
        String out = "{";
        out += "\"stringId\": \"" + blockId + "\"";
        if (blockEntityId != null)
            out += ",\"blockEntityId\": \"" + blockEntityId + "\"";
        if (blockEntityParams != null)
            out += ",\"blockEntityParams\": " + gson.toJson(blockEntityParams);
        if (defaultProperties != null)
            out += ",\"defaultProperties\": " + gson.toJson(defaultProperties);
        out += ",\"blockStates\": " + json.toJson(blockStates);
        out += "}";

        String j = JsonObject.readHjson(out).toString(Stringify.FORMATTED);
        System.out.println(j);

        return j;
    }
}
