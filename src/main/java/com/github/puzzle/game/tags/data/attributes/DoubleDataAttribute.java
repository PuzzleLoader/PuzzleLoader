package com.github.puzzle.game.tags.data.attributes;

import com.github.puzzle.game.tags.data.DataTag;
import finalforeach.cosmicreach.savelib.crbin.CRBinDeserializer;
import finalforeach.cosmicreach.savelib.crbin.CRBinSerializer;

public class DoubleDataAttribute implements DataTag.DataTagAttribute<Double> {

    double data;

    public DoubleDataAttribute() {}

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
    public String toString() {
        return getFormattedString();
    }

    @Override
    public DataTag.DataTagAttribute<Double> copyAndSetValue(Double value) {
        return new DoubleDataAttribute(value);
    }

    @Override
    public String getFormattedString() {
        return Double.toString(data);
    }

    @Override
    public void read(CRBinDeserializer crBinDeserializer) {
        this.data = crBinDeserializer.readDoubleArray("data_value")[0];
    }

    @Override
    public void write(CRBinSerializer crBinSerializer) {
        crBinSerializer.writeDoubleArray("data_value", new double[]{data});
    }

}