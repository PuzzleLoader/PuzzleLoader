package com.github.puzzle.game.engine.blocks;

import com.github.puzzle.game.block.IModBlock;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.util.Identifier;

public class BlockLoadException extends RuntimeException {

    public final IModBlock iModBlock;
    public final Identifier blockId;
    public final String json;
    public final Block block;

    public BlockLoadException(IModBlock iModBlock, Identifier blockId, String json, Block block, Throwable cause) {
        super(cause);
        this.iModBlock = iModBlock;
        this.blockId = blockId;
        this.json = json;
        this.block = block;
    }
}