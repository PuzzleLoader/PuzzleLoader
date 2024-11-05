package com.github.puzzle.game.engine.blocks.model;

import com.github.puzzle.core.loader.util.Reflection;
import com.github.puzzle.game.block.generators.model.BlockModelGenerator;
import com.github.puzzle.game.engine.blocks.models.ServerPuzzleBlockModel;
import finalforeach.cosmicreach.util.Identifier;

import java.util.Map;

public interface IPuzzleBlockModel {
    static IPuzzleBlockModel newReal() {
        return (IPuzzleBlockModel) Reflection.newInstance("com.github.puzzle.game.engine.blocks.models.PuzzleBlockModel");
    }

    static IPuzzleBlockModel newDummy() {
        return new ServerPuzzleBlockModel();
    }

    void registerVanillaTextures(Map<String, Identifier> vanillaTextures);
    void fromModelGenerator(BlockModelGenerator blockModelGenerator);

    void initTextures();

    String getParent();

    String getModelName();

    int getXZRotation();

    void initialize();
}
