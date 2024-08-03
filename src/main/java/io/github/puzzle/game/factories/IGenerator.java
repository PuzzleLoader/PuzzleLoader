package io.github.puzzle.game.factories;

import io.github.puzzle.game.engine.blocks.BlockLoader;

public interface IGenerator {
    void register(BlockLoader loader);
    String generateJson();
}
