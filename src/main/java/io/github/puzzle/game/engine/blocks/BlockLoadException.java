package io.github.puzzle.game.engine.blocks;

import finalforeach.cosmicreach.blocks.Block;
import io.github.puzzle.core.Identifier;
import io.github.puzzle.game.block.IModBlock;

public class BlockLoadException extends RuntimeException {

    public final IModBlock iModBlock;
    public final String blockName;
    public final Identifier blockId;
    public final String json;
    public final Block block;

    public BlockLoadException(IModBlock iModBlock, String blockName, Identifier blockId, String json, Block block, Throwable cause) {
        super(cause);
        this.iModBlock = iModBlock;
        this.blockName = blockName;
        this.blockId = blockId;
        this.json = json;
        this.block = block;
    }
}