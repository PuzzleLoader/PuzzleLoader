package com.github.puzzle.game.items.impl;

import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.data.DataTagManifest;
import finalforeach.cosmicreach.util.Identifier;

public class BasicTool implements IModItem {

    Identifier toolId;
    Identifier toolResource;
    DataTagManifest tagManifest = new DataTagManifest();

    public BasicTool(Identifier id) {
        toolId = id;
        tagManifest.addTag(IModItem.MODEL_ID_PRESET.createTag(IModItem.MODEL_2_5D_ITEM));
        tagManifest.addTag(IModItem.TEXTURE_LOCATION_PRESET.createTag(Identifier.of(id.getNamespace(), "textures/items/" + id.getName() + ".png")));
    }

    public BasicTool(Identifier id, Identifier location) {
        toolId = id;
        toolResource = location;
        tagManifest.addTag(IModItem.MODEL_ID_PRESET.createTag(IModItem.MODEL_2_5D_ITEM));
        tagManifest.addTag(IModItem.TEXTURE_LOCATION_PRESET.createTag(location));
    }

    @Override
    public DataTagManifest getTagManifest() {
        return tagManifest;
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
    public boolean isTool() {
        return true;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean isCatalogHidden() {
        return false;
    }

    @Override
    public boolean hasIntProperty(String s) {
        return tagManifest.hasTag(s);
    }

    @Override
    public int getIntProperty(String s, int i) {
        if (tagManifest.hasTag(s)) return (int) tagManifest.getTag(s).getValue();
        return i;
    }

    @Override
    public String getName() {
        return toolId.getName();
    }
}
