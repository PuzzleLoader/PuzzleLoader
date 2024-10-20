package com.github.puzzle.game.engine.blocks.model;

import com.github.puzzle.game.factories.IGenerator;

public interface IBlockModelGenerator extends IGenerator {
    String getModelName();

    String generateJson();
}
