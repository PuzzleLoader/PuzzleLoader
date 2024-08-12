package com.github.puzzle.game.items.data.attributes;

import com.github.puzzle.core.Identifier;
import com.github.puzzle.core.resources.ResourceLocation;
import com.github.puzzle.game.items.data.DataTag;

public class ResourceLocationDataAttribute implements DataTag.DataTagAttribute<ResourceLocation> {

    ResourceLocation value;

    public ResourceLocationDataAttribute(ResourceLocation value) {
        this.value = value;
    }

    @Override
    public void setValue(ResourceLocation value) {
        this.value = value;
    }

    @Override
    public ResourceLocation getValue() {
        return value;
    }

    @Override
    public DataTag.DataTagAttribute<ResourceLocation> copyAndSetValue(ResourceLocation value) {
        return new ResourceLocationDataAttribute(value);
    }

}
