package com.github.puzzle.game.block.generation;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.badlogic.gdx.utils.OrderedMap;
import finalforeach.cosmicreach.blocks.BlockPlaceCheck;
import finalforeach.cosmicreach.util.JsonPredicateParser;

import java.util.function.Predicate;

public class BlockGenerator {

    public static class State implements Json.Serializable {

        public String modelName;
        public int lightLevelRed = 0;
        public int lightLevelGreen = 0;
        public int lightLevelBlue = 0;
        public int lightAttenuation = 15;
        public String langKey;
        public String blockEventsId = "base:block_events_default";
        public float blastResistance = 100.0F;
        @Deprecated
        public boolean generateSlabs = false;
        public String[] stateGenerators = null;
        public boolean catalogHidden = false;
        public boolean isOpaque = true;
        public boolean walkThrough = false;
        public boolean itemCatalogHidden = false;
        public boolean canRaycastForBreak = true;
        public boolean canRaycastForPlaceOn = true;
        public boolean canRaycastForReplace = false;
        public boolean isFluid = false;
        public boolean allowSwapping = false;
        public String[] tags;
        public String swapGroupId;
        public String dropId;
        public float hardness = 1.5F;
        String canPlace = null;
        public Predicate<BlockPlaceCheck> canPlaceCheck = JsonPredicateParser.getAlwaysTrue();
        int rotXZ = 0;
        public OrderedMap<String, ?> dropParams;
        ObjectIntMap<String> intProperties = new ObjectIntMap();

        @Override
        public void write(Json json) {

        }

        @Override
        public void read(Json json, JsonValue jsonValue) {

        }
    }

}
