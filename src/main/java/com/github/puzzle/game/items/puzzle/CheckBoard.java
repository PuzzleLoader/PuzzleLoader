package com.github.puzzle.game.items.puzzle;

import finalforeach.cosmicreach.util.Identifier;
import com.github.puzzle.core.Puzzle;
import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.data.DataTagManifest;

public class CheckBoard implements IModItem {

    Identifier id = Identifier.of(Puzzle.MOD_ID, "checker_board");
    DataTagManifest tagManifest = new DataTagManifest();

    public CheckBoard() {
        tagManifest.addTag(IModItem.IS_DEBUG_ATTRIBUTE.createTag(true));
        tagManifest.addTag(IModItem.MODEL_ID_PRESET.createTag(IModItem.MODEL_2_5D_ITEM));
        tagManifest.addTag(IModItem.TEXTURE_LOCATION_PRESET.createTag(Identifier.of(Puzzle.MOD_ID, "checker_board.png")));
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
    public DataTagManifest getTagManifest() {
        return tagManifest;
    }

    @Override
    public String getName() {
        return "Debug Board";
    }
}
