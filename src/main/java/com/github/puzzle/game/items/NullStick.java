package com.github.puzzle.game.items;

import com.github.puzzle.core.Identifier;
import com.github.puzzle.core.Puzzle;
import com.github.puzzle.game.items.data.DataTag;
import com.github.puzzle.game.items.data.DataTagManifest;
import com.github.puzzle.game.items.data.attributes.BooleanDataAttribute;

public class NullStick implements IModItem {

    Identifier id = new Identifier(Puzzle.MOD_ID, "null_stick");
    DataTagManifest tagManifest = new DataTagManifest();

    public static final String IS_DEBUG_ATTRIBUTE = "is_item_debug";

    public NullStick() {
        tagManifest.addTag(new DataTag<>(IS_DEBUG_ATTRIBUTE, new BooleanDataAttribute(true)));
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

}
