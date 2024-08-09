package com.github.puzzle.game.items.data.attributes;

import com.github.puzzle.game.items.data.DataTag;

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

}
