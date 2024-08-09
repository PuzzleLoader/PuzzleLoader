package com.github.puzzle.game.items.data.attributes;

import com.badlogic.gdx.math.Vector2;
import com.github.puzzle.game.items.data.DataTag;

public class Vector2DataAttribute implements DataTag.DataTagAttribute<Vector2> {

    Vector2 data;

    public Vector2DataAttribute(Vector2 data) {
        this.data = data;
    }

    @Override
    public void setValue(Vector2 value) {
        this.data = value;
    }

    @Override
    public Vector2 getValue() {
        return data;
    }

}
