package com.github.puzzle.game.engine.blocks;

import com.badlogic.gdx.utils.Queue;
import com.github.puzzle.game.block.IModBlock;

public class ServerBlockLoader implements IBlockLoader {

    Queue<IModBlock> BLOCKS_TO_BE_PROCESSED = new Queue<>();

    @Override
    public void add(IModBlock block) {
        BLOCKS_TO_BE_PROCESSED.addLast(block);
    }

    @Override
    public void process() {
        while (!BLOCKS_TO_BE_PROCESSED.isEmpty()) {
            IModBlock block = BLOCKS_TO_BE_PROCESSED.removeFirst();


        }
    }


}
