package com.github.puzzle.game.block.generators;

import finalforeach.cosmicreach.util.Identifier;

public class PassThroughBlockGenerator extends BlockGenerator {

    String json;

    public PassThroughBlockGenerator(Identifier blockId, String json) {
        super(blockId);
        this.json = json;
    }

    @Override
    public String generateJson() {
        return json;
    }
}
