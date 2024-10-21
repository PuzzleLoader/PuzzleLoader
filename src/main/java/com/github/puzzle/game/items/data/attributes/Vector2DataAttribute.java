package com.github.puzzle.game.items.data.attributes;

import com.badlogic.gdx.math.Vector2;
import com.github.puzzle.game.items.data.DataTag;
import finalforeach.cosmicreach.savelib.crbin.CRBinDeserializer;
import finalforeach.cosmicreach.savelib.crbin.CRBinSerializer;

public class Vector2DataAttribute implements DataTag.DataTagAttribute<Vector2> {

    Vector2 data;

    public Vector2DataAttribute() {}

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

    @Override
    public DataTag.DataTagAttribute<Vector2> copyAndSetValue(Vector2 value) {
        return new Vector2DataAttribute(value);
    }

    @Override
    public String getFormattedString() {
        return data.toString();
    }

    @Override
    public String toString() {
        return getFormattedString();
    }

    @Override
    public void read(CRBinDeserializer crBinDeserializer) {
        Vector2 vector = new Vector2();
        vector.x = crBinDeserializer.readFloat("data_value_x", 0);
        vector.y = crBinDeserializer.readFloat("data_value_y", 0);
        this.data = vector;
    }

    @Override
    public void write(CRBinSerializer crBinSerializer) {
        crBinSerializer.writeFloat("data_value_x", data.x);
        crBinSerializer.writeFloat("data_value_y", data.y);
    }

}
