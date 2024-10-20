package com.github.puzzle.game.engine.blocks.actions;

import com.github.puzzle.game.PuzzleRegistries;
import com.github.puzzle.game.block.IModBlock;
import finalforeach.cosmicreach.blockevents.BlockEventArgs;
import finalforeach.cosmicreach.blockevents.actions.ActionId;
import finalforeach.cosmicreach.blockevents.actions.IBlockAction;
import finalforeach.cosmicreach.util.Identifier;

@ActionId(id = "puzzle-loader:mod_block_break")
public class OnBreakTrigger implements IBlockAction {

    public Identifier blockId;

    @Override
    public void act(BlockEventArgs args) {
        IModBlock block = PuzzleRegistries.BLOCKS.get(blockId);
        block.onBreak(args);
    }
}