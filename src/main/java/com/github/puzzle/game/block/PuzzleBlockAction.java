package com.github.puzzle.game.block;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import finalforeach.cosmicreach.util.Identifier;
import finalforeach.cosmicreach.blockevents.BlockEventTrigger;
import finalforeach.cosmicreach.blockevents.actions.IBlockAction;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Zone;
import org.hjson.JsonObject;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public abstract class PuzzleBlockAction implements IBlockAction {

    public Identifier actionId;
    public HashMap<String, org.hjson.JsonValue> parameters = new HashMap<>();

    @Override
    public void act(BlockState blockState, BlockEventTrigger blockEventTrigger, Zone zone, Map<String, Object> map) {
        act(blockState, zone, (BlockPosition) map.get("blockPos"), map);
    }

    protected abstract void act(BlockState blockState, Zone zone, @Nullable BlockPosition pos, Map<String, Object> args);


    @Override
    public void write(Json json) {}

    @Override
    public void read(Json json, JsonValue jsonData) {
        // Fuck GdxJson
        JsonObject object = JsonObject.readHjson(jsonData.toString()).asObject();
        actionId = Identifier.of(object.getString("actionId", ""));

        JsonObject params = object.get("parameters").asObject();
        for (String name : params.names()) {
            parameters.put(name, params.get(name));
        }
    }

    public HashMap<String, org.hjson.JsonValue> getParameters() {
        return parameters;
    }

    @Override
    public String getActionId() {
        return actionId.toString();
    }
}