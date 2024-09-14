package com.github.puzzle.game.items.data.attributes;

import finalforeach.cosmicreach.io.CRBinDeserializer;
import finalforeach.cosmicreach.io.CRBinSerializer;
import com.github.puzzle.core.util.ResourceLocation;
import com.github.puzzle.game.items.data.DataTag;

public class ResourceLocationDataAttribute implements DataTag.DataTagAttribute<ResourceLocation> {

    ResourceLocation value;

    public ResourceLocationDataAttribute() {}

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

    @Override
    public String getFormattedString() {
        return value.toString();
    }

    @Override
    public String toString() {
        return getFormattedString();
    }

    @Override
    public void read(CRBinDeserializer crBinDeserializer) {
        this.value = ResourceLocation.fromString(crBinDeserializer.readString("data_value"));
    }

    @Override
    public void write(CRBinSerializer crBinSerializer) {
        crBinSerializer.writeString("data_value", value.toString());
    }

}
