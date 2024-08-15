package com.github.puzzle.game.items.data.attributes;

import com.github.puzzle.game.items.data.DataTag;
import finalforeach.cosmicreach.io.CRBinDeserializer;
import finalforeach.cosmicreach.io.CRBinSerializer;

public class IntDataAttribute implements DataTag.DataTagAttribute<Integer> {

    int data;

    public IntDataAttribute(int data) {
        this.data = data;
    }

    public IntDataAttribute(Integer data) {
        this.data = data;
    }

    @Override
    public void setValue(Integer value) {
        this.data = value;
    }

    @Override
    public Integer getValue() {
        return data;
    }

    @Override
    public DataTag.DataTagAttribute<Integer> copyAndSetValue(Integer value) {
        return new IntDataAttribute(value);
    }

    @Override
    public String getFormattedString() {
        return Integer.toString(data);
    }

    @Override
    public void read(CRBinDeserializer crBinDeserializer) {
        this.data = crBinDeserializer.readInt("data_value", 0);
    }

    @Override
    public void write(CRBinSerializer crBinSerializer) {
        crBinSerializer.writeInt("data_value", data);
    }
}
