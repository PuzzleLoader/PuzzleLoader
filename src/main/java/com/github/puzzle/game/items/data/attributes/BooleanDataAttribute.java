package com.github.puzzle.game.items.data.attributes;

import com.github.puzzle.game.items.data.DataTag;

public class BooleanDataAttribute implements DataTag.DataTagAttribute<Boolean> {

    boolean data;

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

}
