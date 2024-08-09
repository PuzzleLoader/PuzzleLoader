package com.github.puzzle.game.items.data.attributes;

import com.github.puzzle.game.items.data.DataTag;

public class DoubleDataAttribute implements DataTag.DataTagAttribute<Double> {

    double data;

    public DoubleDataAttribute(double data) {
        this.data = data;
    }

    public DoubleDataAttribute(Double data) {
        this.data = data;
    }

    @Override
    public void setValue(Double value) {
        this.data = value;
    }

    @Override
    public Double getValue() {
        return data;
    }

    @Override
    public DataTag.DataTagAttribute<Double> copyAndSetValue(Double value) {
        return new DoubleDataAttribute(value);
    }

}
