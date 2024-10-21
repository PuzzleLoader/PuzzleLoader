package com.github.puzzle.game.items.data.attributes;

import com.badlogic.gdx.math.Vector3;
import com.github.puzzle.game.items.data.DataTag;
import finalforeach.cosmicreach.savelib.crbin.CRBinDeserializer;
import finalforeach.cosmicreach.savelib.crbin.CRBinSerializer;

public class Vector3DataAttribute implements DataTag.DataTagAttribute<Vector3> {

    Vector3 data;

    public Vector3DataAttribute() {}

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

    @Override
    public DataTag.DataTagAttribute<Vector3> copyAndSetValue(Vector3 value) {
        return new Vector3DataAttribute(value);
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
        Vector3 vector = new Vector3();
        vector.x = crBinDeserializer.readFloat("data_value_x", 0);
        vector.y = crBinDeserializer.readFloat("data_value_y", 0);
        vector.z = crBinDeserializer.readFloat("data_value_z", 0);
        this.data = vector;
    }

    @Override
    public void write(CRBinSerializer crBinSerializer) {
        crBinSerializer.writeFloat("data_value_x", data.x);
        crBinSerializer.writeFloat("data_value_y", data.y);
        crBinSerializer.writeFloat("data_value_z", data.z);
    }

}
