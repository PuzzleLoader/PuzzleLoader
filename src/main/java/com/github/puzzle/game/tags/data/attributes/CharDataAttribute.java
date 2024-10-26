package com.github.puzzle.game.tags.data.attributes;

import com.github.puzzle.game.tags.data.DataTag;
import finalforeach.cosmicreach.savelib.crbin.CRBinDeserializer;
import finalforeach.cosmicreach.savelib.crbin.CRBinSerializer;

public class CharDataAttribute implements DataTag.DataTagAttribute<Character> {

    char data;

    public CharDataAttribute() {}

    public CharDataAttribute(char data) {
        this.data = data;
    }

    public CharDataAttribute(Character data) {
        this.data = data;
    }

    @Override
    public void setValue(Character value) {
        this.data = value;
    }

    @Override
    public Character getValue() {
        return data;
    }

    @Override
    public DataTag.DataTagAttribute<Character> copyAndSetValue(Character value) {
        return new CharDataAttribute(value);
    }

    @Override
    public String getFormattedString() {
        return Character.toString(data);
    }

    @Override
    public String toString() {
        return getFormattedString();
    }

    @Override
    public void read(CRBinDeserializer crBinDeserializer) {
        this.data = (char) crBinDeserializer.readShort("data_value", (short) 0);
    }

    @Override
    public void write(CRBinSerializer crBinSerializer) {
        crBinSerializer.writeShort("data_value", (short) data);
    }
}