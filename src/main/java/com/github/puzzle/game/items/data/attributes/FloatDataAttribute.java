package com.github.puzzle.game.items.data.attributes;

import com.github.puzzle.game.items.data.DataTag;

public class FloatDataAttribute implements DataTag.DataTagAttribute<Float> {

    float data;

    public FloatDataAttribute(float data) {
        this.data = data;
    }

    public FloatDataAttribute(Float data) {
        this.data = data;
    }

    @Override
    public void setValue(Float value) {
        this.data = value;
    }

    @Override
    public Float getValue() {
        return data;
    }

    @Override
    public DataTag.DataTagAttribute<Float> copyAndSetValue(Float value) {
        return new FloatDataAttribute(value);
    }

}
