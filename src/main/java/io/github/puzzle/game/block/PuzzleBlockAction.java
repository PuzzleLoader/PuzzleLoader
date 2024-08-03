package io.github.puzzle.game.block;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import finalforeach.cosmicreach.blockevents.actions.IBlockAction;
import io.github.puzzle.core.Identifier;

// TODO finish this
public abstract class PuzzleBlockAction implements IBlockAction {

    public Identifier actionId;

    @Override
    public void write(Json json) {}

    @Override
    public void read(Json json, JsonValue jsonData) {
        actionId = Identifier.fromString(json.readValue(String.class, jsonData.get("actionId")));
        // TODO read parameters here
    }

    @Override
    public String getActionId() {
        return actionId.toString();
    }
}