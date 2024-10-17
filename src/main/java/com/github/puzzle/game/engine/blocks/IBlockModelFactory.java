package com.github.puzzle.game.engine.blocks;

import finalforeach.cosmicreach.rendering.blockmodels.BlockModel;
import finalforeach.cosmicreach.rendering.blockmodels.IBlockModelInstantiator;

import java.util.List;

public interface IBlockModelFactory extends IBlockModelInstantiator {

    BlockModel createFromJson(String modelName, int rotXZ, String modelJson);

    List<BlockModel> sort();
}
