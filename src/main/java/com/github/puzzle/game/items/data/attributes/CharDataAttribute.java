package com.github.puzzle.game.items.data.attributes;

import com.github.puzzle.game.items.data.DataTag;
import finalforeach.cosmicreach.io.CRBinDeserializer;
import finalforeach.cosmicreach.io.CRBinSerializer;

public class CharDataAttribute implements DataTag.DataTagAttribute<Character> {

    char data;

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
    public void read(CRBinDeserializer crBinDeserializer) {
        this.data = (char) crBinDeserializer.readShort("data_value", (short) 0);
    }

    @Override
    public void write(CRBinSerializer crBinSerializer) {
        crBinSerializer.writeShort("data_value", (short) data);
    }
}
