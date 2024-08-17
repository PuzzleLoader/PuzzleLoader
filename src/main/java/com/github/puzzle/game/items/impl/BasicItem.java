package com.github.puzzle.game.items.impl;

import com.github.puzzle.core.Identifier;
import com.github.puzzle.core.resources.ResourceLocation;
import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.data.DataTagManifest;

public class BasicItem implements IModItem {

    Identifier toolId;
    ResourceLocation toolResource;
    DataTagManifest tagManifest = new DataTagManifest();

    public BasicItem(Identifier id) {
        toolId = id;
        tagManifest.addTag(IModItem.MODEL_ID_PRESET.createTag(IModItem.MODEL_2_5D_ITEM));
        tagManifest.addTag(IModItem.TEXTURE_LOCATION_PRESET.createTag(new ResourceLocation(id.namespace, "textures/items/" + id.name + ".png")));
    }

    public BasicItem(Identifier model_Id, Identifier id) {
        toolId = id;
        tagManifest.addTag(IModItem.MODEL_ID_PRESET.createTag(model_Id));
        tagManifest.addTag(IModItem.TEXTURE_LOCATION_PRESET.createTag(new ResourceLocation(id.namespace, "textures/items/" + id.name + ".png")));
    }

    public BasicItem(Identifier id, ResourceLocation location) {
        toolId = id;
        toolResource = location;
        tagManifest.addTag(IModItem.MODEL_ID_PRESET.createTag(IModItem.MODEL_2_5D_ITEM));
        tagManifest.addTag(IModItem.TEXTURE_LOCATION_PRESET.createTag(location));
    }

    public BasicItem(Identifier model_Id, Identifier id, ResourceLocation location) {
        toolId = id;
        toolResource = location;
        tagManifest.addTag(IModItem.MODEL_ID_PRESET.createTag(model_Id));
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
}
