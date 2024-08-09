package com.github.puzzle.game.items.data.attributes;

import com.github.puzzle.game.items.data.DataTag;

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

}
