package com.github.puzzle.game.block;

import com.badlogic.gdx.utils.Json;
import com.github.puzzle.core.resources.PuzzleGameAssetLoader;
import finalforeach.cosmicreach.util.Identifier;
import com.github.puzzle.core.resources.VanillaAssetLocations;
import com.github.puzzle.game.generators.BlockGenerator;
import com.github.puzzle.game.generators.BlockModelGenerator;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @see IModBlock
 * This class allows loading regular Json files
 * as IModBlocks
 */
public class DataModBlock implements IModBlock {

    public static class JsonBlock {
        public String stringId;
        public LinkedHashMap<String, String> defaultParams;
        public LinkedHashMap<String, BlockGenerator.State> blockStates;
        public String blockEntityId;
        public LinkedHashMap<String, ?> blockEntityParams;
    }

    public Identifier debugResourceLocation;
    public String blockJson;
    private Identifier identifier;

    public DataModBlock(Identifier json) {
        this(PuzzleGameAssetLoader.locateAsset(Identifier.of(json.getNamespace(), json.getName().startsWith("blocks/") ? json.getName() : "blocks/" + json.getName())).readString());
        this.debugResourceLocation = json;
    }

    public DataModBlock(String blockJson) {
        this.blockJson = blockJson;
    }

    @Override
    public Identifier getIdentifier() {
        return identifier;
    }

    @Override
    public BlockGenerator getBlockGenerator() {
        Json json = new Json();
        JsonBlock block = json.fromJson(JsonBlock.class, blockJson);
        identifier = Identifier.of(block.stringId);
        BlockGenerator generator = new BlockGenerator(getIdentifier());
        generator.blockEntityId = block.blockEntityId;
        generator.blockEntityParams = block.blockEntityParams;
        generator.defaultParams = block.defaultParams;
        generator.blockStates = block.blockStates;
        return generator;
    }

    @Override
    public List<BlockModelGenerator> getBlockModelGenerators(Identifier blockId) {
        return List.of();
    }
}