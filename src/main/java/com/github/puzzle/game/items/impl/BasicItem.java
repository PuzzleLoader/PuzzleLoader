package com.github.puzzle.game.items.impl;

import com.github.puzzle.core.Identifier;
import com.github.puzzle.core.Puzzle;
import com.github.puzzle.core.resources.ResourceLocation;
import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.data.DataTagManifest;

public class BasicItem implements IModItem {

    Identifier toolId;
    ResourceLocation toolResource;
    DataTagManifest tagManifest = new DataTagManifest();

    public BasicItem(Identifier id) {
        toolId = id;
        tagManifest.addTag(IModItem.MODEL_ID_PRESET.createTag(IModItem.MODEL_2D_ITEM));
        tagManifest.addTag(IModItem.TEXTURE_LOCATION_PRESET.createTag(new ResourceLocation(id.namespace, "textures/items/" + id.name + ".png")));
    }

    public BasicItem(Identifier id, ResourceLocation location) {
        toolId = id;
        toolResource = location;
    }

    @Override
    public Identifier getIdentifier() {
        return toolId;
    }

    @Override
    public String toString() {
        return getID();
    }

    @Override
    public boolean isCatalogHidden() {
        return false;
    }
}
