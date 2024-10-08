package com.github.puzzle.game.block.generators.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import finalforeach.cosmicreach.util.Identifier;

public class SingleColorModelGenerator extends BlockModelGenerator {
    public SingleColorModelGenerator(Identifier blockId, String modelName, Color color) {
        super(blockId, modelName);
        Pixmap texture = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        texture.setColor(color);
        texture.fill();
        createTexture("all", texture);
        createCuboid(0, 0, 0, 16, 16, 16, "all");
    }
}
