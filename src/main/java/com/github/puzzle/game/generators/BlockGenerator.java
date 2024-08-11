package com.github.puzzle.game.generators;

import com.badlogic.gdx.utils.Json;
import com.github.puzzle.core.Identifier;
import com.github.puzzle.game.engine.blocks.BlockLoader;
import com.github.puzzle.game.factories.IGenerator;
import com.github.puzzle.game.oredict.tags.Tag;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BlockGenerator implements IGenerator {

    public static class State {
        /**
         * modelName is used for locating the model used for this block
         * for blocks using models in vanilla json files the modelName is
         * a ResourceLocation
         * for blocks using custom models, the modelName is a combination
         * of blockId and modelName
         * this is handled by setting the usingBlockModelGenerator flag in
         * createBlockState
         */
        public String modelName;
        public int lightLevelRed = 0;
        public int lightLevelGreen = 0;
        public int lightLevelBlue = 0;
        public int lightAttenuation = 15;
        /**
         * better name would be eventName
         */
        public String blockEventsId = "base:block_events_default";
        public float blastResistance = 100.0F;
        @Deprecated
        public boolean generateSlabs = false;
        public String[] stateGenerators = null;
        public boolean catalogHidden = false;
        public boolean isTransparent = false;
        public boolean isOpaque = true;
        public boolean walkThrough = false;
        public boolean cullsSelf = true;
        public boolean itemCatalogHidden = false;
        public boolean canRaycastForBreak = true;
        public boolean canRaycastForPlaceOn = true;
        public boolean canRaycastForReplace = false;
        public boolean isFluid = false;
        public boolean allowSwapping = false;

        public String swapGroupId;
        public String dropId;

    }

    public Identifier blockId;
    public String blockName;

    public String blockEntityId;
    public Map<String, ?> blockEntityParams;

    public Map<String, String> defaultParams;
    public Map<String, State> blockStates;
    public List<Tag> itemTags;

    public BlockGenerator(Identifier blockId, String blockName) {
        this.blockId = blockId;
        this.blockName = blockName;
        this.defaultParams = new LinkedHashMap<>();
        this.blockStates = new LinkedHashMap<>();
    }

    public void addBlockEntity(String blockEntityId, Map<String, ?> parameters) {
        this.blockEntityId = blockEntityId;
        this.blockEntityParams = parameters;
    }

    public State createBlockState(String id, String modelName, boolean usingBlockModelGenerator) {
        State state = new State();
        state.modelName = usingBlockModelGenerator ? BlockModelGenerator.getModelName(blockId, modelName) : modelName;
        state.blockEventsId = BlockEventGenerator.getEventName(blockId, "puzzle_default");
        blockStates.put(id, state);
        return state;
    }
    
    public State createBlockState(String id, String modelName, boolean usingBlockModelGenerator, String eventName, boolean usingBlockEventGenerator) {
        State state = new State();
        state.modelName = usingBlockModelGenerator ? BlockModelGenerator.getModelName(blockId, modelName) : modelName;
        state.blockEventsId = usingBlockEventGenerator ? BlockEventGenerator.getEventName(blockId, eventName) : eventName;
        blockStates.put(id, state);
        return state;
    }

    @Override
    public void register(BlockLoader loader) {}

    public void addTags(Tag ...itemTag) {
        itemTags.addAll(List.of(itemTag));
    }

    @Override
    public String generateJson() {
        Json json = new Json();
        json.setTypeName(null);
        Json json2 = new Json();
        return """
                {"stringId":%s,"blockEntityId":%s,"blockEntityParams":%s,"blockStates":%s}
                """.formatted(blockId.toString(), blockEntityId, json2.toJson(blockEntityParams), json.toJson(blockStates));
    }
}
