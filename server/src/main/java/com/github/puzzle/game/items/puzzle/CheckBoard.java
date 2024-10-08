package com.github.puzzle.game.items.puzzle;

import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.data.DataTagManifest;
import finalforeach.cosmicreach.util.Identifier;

import static com.github.puzzle.core.Constants.MOD_ID;

public class CheckBoard implements IModItem {

    Identifier id = Identifier.of(MOD_ID, "checker_board");
    DataTagManifest tagManifest = new DataTagManifest();

    public CheckBoard() {
        tagManifest.addTag(IModItem.IS_DEBUG_ATTRIBUTE.createTag(true));
        tagManifest.addTag(IModItem.MODEL_ID_PRESET.createTag(IModItem.MODEL_2_5D_ITEM));
        tagManifest.addTag(IModItem.TEXTURE_LOCATION_PRESET.createTag(Identifier.of(MOD_ID, "checker_board.png")));
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
