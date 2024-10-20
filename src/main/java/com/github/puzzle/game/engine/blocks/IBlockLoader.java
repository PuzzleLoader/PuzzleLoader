package com.github.puzzle.game.engine.blocks;

import com.badlogic.gdx.graphics.Pixmap;
import finalforeach.cosmicreach.util.Identifier;

public interface IBlockLoader {
    void registerTexture(String modelTextureName, Pixmap pixmap);

    void registerTexture(Identifier identifier);
}
