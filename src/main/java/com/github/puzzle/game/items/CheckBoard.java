package com.github.puzzle.game.items;

import com.github.puzzle.core.Identifier;
import com.github.puzzle.core.Puzzle;
import com.github.puzzle.core.resources.ResourceLocation;
import com.github.puzzle.game.items.data.DataTagManifest;

public class CheckBoard implements IModItem {

    Identifier id = new Identifier(Puzzle.MOD_ID, "checker_board");
    DataTagManifest tagManifest = new DataTagManifest();

    public CheckBoard() {
        tagManifest.addTag(NullStick.IS_DEBUG_ATTRIBUTE.createTag(true));
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
    public ResourceLocation getTexturePath() {
        return new ResourceLocation(Puzzle.MOD_ID, "textures/items/checker_board.png");
    }

    @Override
    public DataTagManifest getTagManifest() {
        return tagManifest;
    }

    @Override
    public boolean isCatalogHidden() {
        return false;
    }
}
