package com.github.puzzle.game.block;

import com.badlogic.gdx.graphics.Pixmap;
import com.github.puzzle.game.block.generators.BlockGenerator;
import finalforeach.cosmicreach.util.Identifier;

/**
 * @see IModBlock
 * This class creates a basic Block with top, bottom and side
 * textures, a simple default block state and a single custom
 * model
 */
public class BasicModBlock implements IModBlock {

    public Identifier blockId;
    public String blockName;
    public Pixmap top, bottom, side;

    public BasicModBlock(Identifier blockId, String blockName) {
        this.blockId = blockId;
        this.blockName = blockName;
    }

    @Override
    public Identifier getIdentifier() {
        return blockId;
    }

    @Override
    public BlockGenerator getBlockGenerator() {
        BlockGenerator generator = new BlockGenerator(blockId);
        generator.createBlockState("default", "model", true);
        return generator;
    }

}