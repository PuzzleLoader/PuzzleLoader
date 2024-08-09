package com.github.puzzle.game.items.data.attributes;

import com.badlogic.gdx.math.Vector3;
import com.github.puzzle.game.items.data.DataTag;

public class Vector3DataAttribute implements DataTag.DataTagAttribute<Vector3> {

    Vector3 data;

    public Vector3DataAttribute(Vector3 data) {
        this.data = data;
    }

    @Override
    public void setValue(Vector3 value) {
        this.data = value;
    }

    @Override
    public Vector3 getValue() {
        return data;
    }

}
