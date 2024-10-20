package com.github.puzzle.game.factories;

import com.github.puzzle.game.engine.blocks.IBlockLoader;

public interface IGenerator {
    void register(IBlockLoader loader);
    String generateJson();
}
