package com.github.puzzle.game.tags.data.attributes;

import com.github.puzzle.game.tags.data.DataTag;
import finalforeach.cosmicreach.savelib.crbin.CRBinDeserializer;
import finalforeach.cosmicreach.savelib.crbin.CRBinSerializer;

public class FloatDataAttribute implements DataTag.DataTagAttribute<Float> {

    float data;

    public FloatDataAttribute() {}

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
    public String toString() {
        return getFormattedString();
    }

    @Override
    public DataTag.DataTagAttribute<Float> copyAndSetValue(Float value) {
        return new FloatDataAttribute(value);
    }

    @Override
    public String getFormattedString() {
        return Float.toString(data);
    }

    @Override
    public void read(CRBinDeserializer crBinDeserializer) {
        this.data = crBinDeserializer.readFloat("data_value", 0);
    }

    @Override
    public void write(CRBinSerializer crBinSerializer) {
        crBinSerializer.writeFloat("data_value", data);
    }

}