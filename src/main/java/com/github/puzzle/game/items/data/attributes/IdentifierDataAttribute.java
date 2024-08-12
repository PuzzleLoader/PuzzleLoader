package com.github.puzzle.game.items.data.attributes;

import com.github.puzzle.core.Identifier;
import com.github.puzzle.game.items.data.DataTag;

public class IdentifierDataAttribute implements DataTag.DataTagAttribute<Identifier> {

    Identifier value;

    public IdentifierDataAttribute(Identifier value) {
        this.value = value;
    }

    @Override
    public void setValue(Identifier value) {
        this.value = value;
    }

    @Override
    public Identifier getValue() {
        return value;
    }

    @Override
    public DataTag.DataTagAttribute<Identifier> copyAndSetValue(Identifier value) {
        return new IdentifierDataAttribute(value);
    }

}
