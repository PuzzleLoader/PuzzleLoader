package com.github.puzzle.game.items.data.attributes;

import com.github.puzzle.game.items.data.DataTag;
import finalforeach.cosmicreach.io.CRBinDeserializer;
import finalforeach.cosmicreach.io.CRBinSerializer;

public class StringDataAttribute implements DataTag.DataTagAttribute<String> {

    String data;

    public StringDataAttribute() {}

    public StringDataAttribute(String data) {
        this.data = data;
    }

    @Override
    public void setValue(String value) {
        this.data = value;
    }

    @Override
    public String getValue() {
        return data;
    }

    @Override
    public DataTag.DataTagAttribute<String> copyAndSetValue(String value) {
        return new StringDataAttribute(value);
    }

    @Override
    public String getFormattedString() {
        return data;
    }

    @Override
    public String toString() {
        return getFormattedString();
    }

    @Override
    public void read(CRBinDeserializer crBinDeserializer) {
        this.data = crBinDeserializer.readString("data_value");
    }

    @Override
    public void write(CRBinSerializer crBinSerializer) {
        crBinSerializer.writeString("data_value", data);
    }

}
