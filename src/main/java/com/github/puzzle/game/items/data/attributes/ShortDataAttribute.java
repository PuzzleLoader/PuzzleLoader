package com.github.puzzle.game.items.data.attributes;

import com.github.puzzle.game.items.data.DataTag;
import finalforeach.cosmicreach.io.CRBinDeserializer;
import finalforeach.cosmicreach.io.CRBinSerializer;

public class ShortDataAttribute implements DataTag.DataTagAttribute<Short> {

    short data;

    public ShortDataAttribute(short data) {
        this.data = data;
    }

    public ShortDataAttribute(Short data) {
        this.data = data;
    }

    @Override
    public void setValue(Short value) {
        this.data = value;
    }

    @Override
    public Short getValue() {
        return data;
    }

    @Override
    public DataTag.DataTagAttribute<Short> copyAndSetValue(Short value) {
        return new ShortDataAttribute(value);
    }

    @Override
    public String getFormattedString() {
        return Short.toString(data);
    }

    @Override
    public void read(CRBinDeserializer crBinDeserializer) {
        this.data = crBinDeserializer.readShort("data_value", (short) 0);
    }

    @Override
    public void write(CRBinSerializer crBinSerializer) {
        crBinSerializer.writeShort("data_value", data);
    }
}
