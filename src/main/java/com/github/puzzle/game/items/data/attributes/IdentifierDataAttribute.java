package com.github.puzzle.game.items.data.attributes;

import com.github.puzzle.core.Identifier;
import com.github.puzzle.game.items.data.DataTag;
import finalforeach.cosmicreach.io.CRBinDeserializer;
import finalforeach.cosmicreach.io.CRBinSerializer;

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

    @Override
    public String getFormattedString() {
        return value.toString();
    }

    @Override
    public void read(CRBinDeserializer crBinDeserializer) {
        this.value = Identifier.fromString(crBinDeserializer.readString("data_value"));
    }

    @Override
    public void write(CRBinSerializer crBinSerializer) {
        crBinSerializer.writeString("data_value", value.toString());
    }
}
