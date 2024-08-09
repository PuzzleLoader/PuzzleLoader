package com.github.puzzle.game.items.impl;

import com.github.puzzle.core.Identifier;
import com.github.puzzle.core.resources.ResourceLocation;
import com.github.puzzle.game.items.IModItem;

public class BasicItem implements IModItem {

    Identifier toolId;
    ResourceLocation toolResource;

    public BasicItem(Identifier id) {
        toolId = id;
        toolResource = new ResourceLocation(id.namespace, "textures/items/" + id.name + ".png");
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
    public ResourceLocation getTexturePath() {
        return toolResource;
    }

}
