package com.github.puzzle.game.engine.blocks;

import com.github.puzzle.game.block.IModBlock;

public interface IBlockLoader {

    void add(IModBlock block);
    void process();

}
