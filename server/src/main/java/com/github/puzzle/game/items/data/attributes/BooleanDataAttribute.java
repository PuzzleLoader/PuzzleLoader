package com.github.puzzle.game.items.data.attributes;

import com.github.puzzle.game.items.data.DataTag;
import finalforeach.cosmicreach.io.CRBinDeserializer;
import finalforeach.cosmicreach.io.CRBinSerializer;

public class BooleanDataAttribute implements DataTag.DataTagAttribute<Boolean> {

    boolean data;

    public BooleanDataAttribute() {}

    public BooleanDataAttribute(boolean data) {
        this.data = data;
    }

    public BooleanDataAttribute(Boolean data) {
        this.data = data;
    }

    @Override
    public void setValue(Boolean value) {
        this.data = value;
    }

    @Override
    public Boolean getValue() {
        return data;
    }

    @Override
    public DataTag.DataTagAttribute<Boolean> copyAndSetValue(Boolean value) {
        return new BooleanDataAttribute(value);
    }

    @Override
    public String getFormattedString() {
        return Boolean.toString(data);
    }

    @Override
    public String toString() {
        return getFormattedString();
    }

    @Override
    public void read(CRBinDeserializer crBinDeserializer) {
        this.data = crBinDeserializer.readBoolean("data_value", false);
    }

    @Override
    public void write(CRBinSerializer crBinSerializer) {
        crBinSerializer.writeBoolean("data_value", data);
    }
}
