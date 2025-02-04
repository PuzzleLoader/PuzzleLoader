package com.github.puzzle.game.engine.blocks;

import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJson;

public class BlockModelJsonInitializer {

    public static BlockModelJson init(BlockModelJson m) {
        return ((BLOCK_MODEL_JSON_INITIALIZER) m).init();
    }

    public interface BLOCK_MODEL_JSON_INITIALIZER {
        BlockModelJson init();
    }

}
