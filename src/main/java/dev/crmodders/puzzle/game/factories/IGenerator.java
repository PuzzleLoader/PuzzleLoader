package dev.crmodders.puzzle.game.factories;

import dev.crmodders.puzzle.game.engine.blocks.BlockLoader;

public interface IGenerator {
    void register(BlockLoader loader);
    String generateJson();
}
