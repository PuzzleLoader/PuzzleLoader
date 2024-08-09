package com.github.puzzle.game.items.data.attributes;

import com.github.puzzle.game.items.data.DataTag;

public class LongDataAttribute implements DataTag.DataTagAttribute<Long> {

    long data;

    public LongDataAttribute(long data) {
        this.data = data;
    }

    public LongDataAttribute(Long data) {
        this.data = data;
    }

    @Override
    public void setValue(Long value) {
        this.data = value;
    }

    @Override
    public Long getValue() {
        return data;
    }

}
