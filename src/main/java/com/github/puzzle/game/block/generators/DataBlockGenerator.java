package com.github.puzzle.game.block.generators;

import finalforeach.cosmicreach.util.Identifier;

public class DataBlockGenerator extends BlockGenerator {

    private String json;

    public DataBlockGenerator(Identifier blockId, String json) {
        super(blockId);
        this.json = json;
    }

    public DataBlockGenerator(Identifier blockId) {
        super(blockId);
    }

    @Override
    public String generateJson() {
        return json;
    }
}
