package com.github.puzzle.game.items.data.attributes;

import com.github.puzzle.game.items.data.DataTag;

public class ByteDataAttribute implements DataTag.DataTagAttribute<Byte> {

    byte data;

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
}
