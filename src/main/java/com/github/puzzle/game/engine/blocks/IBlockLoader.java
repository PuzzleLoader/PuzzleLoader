package com.github.puzzle.game.engine.blocks;

import com.badlogic.gdx.graphics.Pixmap;
import finalforeach.cosmicreach.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface IBlockLoader {

    Logger LOGGER = LoggerFactory.getLogger("BlockLoader");

    void registerTexture(String modelTextureName, Pixmap pixmap);

    void registerTexture(Identifier identifier);
}
