package com.github.puzzle.game.factories;

import com.github.puzzle.game.engine.blocks.BlockLoader;

public interface IGenerator {
    void register(BlockLoader loader);
    String generateJson();
}
