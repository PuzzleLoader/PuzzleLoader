package com.github.puzzle.game.items;

import com.github.puzzle.core.Identifier;
import com.github.puzzle.core.Puzzle;
import com.github.puzzle.core.resources.ResourceLocation;
import com.github.puzzle.game.items.data.DataTagManifest;

public class BlockWrench implements IModItem {

    Identifier id = new Identifier(Puzzle.MOD_ID, "block_wrench");
    DataTagManifest tagManifest = new DataTagManifest();

    public BlockWrench() {
    }

    @Override
    public String toString() {
        return "Item: " + getID();
    }

    @Override
    public Identifier getIdentifier() {
        return id;
    }

    @Override
    public boolean isTool() {
        return true;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public DataTagManifest getTagManifest() {
        return tagManifest;
    }

    @Override
    public ResourceLocation getTexturePath() {
        return new ResourceLocation(Puzzle.MOD_ID, "textures/items/block_wrench.png");
    }

}
