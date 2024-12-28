package com.github.puzzle.game.engine.blocks;

import com.badlogic.gdx.graphics.Pixmap;
import com.github.puzzle.game.block.IModBlock;
import finalforeach.cosmicreach.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface IBlockLoader {

    Logger LOGGER = LoggerFactory.getLogger("BlockLoader");

    void registerTexture(String modelTextureName, Pixmap pixmap);

    void registerTexture(Identifier identifier);

    Identifier loadBlock(IModBlock block);

    List<BlockLoadException> getErrors();

    void hookOriginalBlockConstants();

    void registerFinalizers();
}
