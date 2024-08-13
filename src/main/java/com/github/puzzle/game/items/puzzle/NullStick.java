package com.github.puzzle.game.items.puzzle;

import com.github.puzzle.core.Identifier;
import com.github.puzzle.core.Puzzle;
import com.github.puzzle.core.resources.ResourceLocation;
import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.data.DataTagManifest;
import com.github.puzzle.game.items.data.DataTagPreset;
import com.github.puzzle.game.items.data.attributes.BooleanDataAttribute;

public class NullStick implements IModItem {

    Identifier id = new Identifier(Puzzle.MOD_ID, "null_stick");
    DataTagManifest tagManifest = new DataTagManifest();

    public static final DataTagPreset<Boolean> IS_DEBUG_ATTRIBUTE = new DataTagPreset<>("is_item_debug", new BooleanDataAttribute(false));

    public NullStick() {
        tagManifest.addTag(IS_DEBUG_ATTRIBUTE.createTag(true));
        tagManifest.addTag(IModItem.MODEL_ID_PRESET.createTag(IModItem.MODEL_2_5D_ITEM));
        tagManifest.addTag(IModItem.TEXTURE_LOCATION_PRESET.createTag(new ResourceLocation(Puzzle.MOD_ID, "textures/items/null_stick.png")));
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
    public boolean isCatalogHidden() {
        return false;
    }
}
