package com.github.puzzle.game.items;

import com.github.puzzle.core.Identifier;
import com.github.puzzle.core.Puzzle;

public class NullStick implements IModItem {

    Identifier id = new Identifier(Puzzle.MOD_ID, "null_stick");

    @Override
    public Identifier getIdentifier() {
        return id;
    }

}
