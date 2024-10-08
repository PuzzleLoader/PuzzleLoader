package com.github.puzzle.game.items.data.attributes;

import com.github.puzzle.game.items.data.DataTag;
import finalforeach.cosmicreach.io.CRBinDeserializer;
import finalforeach.cosmicreach.io.CRBinSerializer;

public class ByteDataAttribute implements DataTag.DataTagAttribute<Byte> {

    byte data;

    public ByteDataAttribute() {}

    public ByteDataAttribute(byte data) {
        this.data = data;
    }

    public ByteDataAttribute(Byte data) {
        this.data = data;
    }

    @Override
    public void setValue(Byte value) {
        this.data = value;
    }

    @Override
    public Byte getValue() {
        return data;
    }

    @Override
    public DataTag.DataTagAttribute<Byte> copyAndSetValue(Byte value) {
        return new ByteDataAttribute(value);
    }

    @Override
    public String getFormattedString() {
        return Byte.toString(data);
    }

    @Override
    public String toString() {
        return getFormattedString();
    }

    @Override
    public void read(CRBinDeserializer crBinDeserializer) {
        this.data = crBinDeserializer.readByteArray("data_value")[0];
    }

    @Override
    public void write(CRBinSerializer crBinSerializer) {
        crBinSerializer.writeByteArray("data_value", new byte[]{data});
    }
}
