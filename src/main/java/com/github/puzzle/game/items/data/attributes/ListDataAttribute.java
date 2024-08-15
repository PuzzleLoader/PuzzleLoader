package com.github.puzzle.game.items.data.attributes;

import com.github.puzzle.game.items.data.DataTag;
import finalforeach.cosmicreach.io.CRBinDeserializer;
import finalforeach.cosmicreach.io.CRBinSerializer;

import java.util.Arrays;
import java.util.List;

public class ListDataAttribute<T> implements DataTag.DataTagAttribute<List<T>> {

    List<T> data;

    public ListDataAttribute(List<T> data) {
        this.data = data;
    }

    @Override
    public void setValue(List<T> value) {
        this.data = value;
    }

    @Override
    public List<T> getValue() {
        return data;
    }

    @Override
    public DataTag.DataTagAttribute<List<T>> copyAndSetValue(List<T> value) {
        return new ListDataAttribute<>(value);
    }

    @Override
    public String getFormattedString() {
        return Arrays.toString(data.toArray());
    }

    @Override
    public void read(CRBinDeserializer crBinDeserializer) {
        throw new RuntimeException("List data attribute not supported for DataTag serialization!");
    }

    @Override
    public void write(CRBinSerializer crBinSerializer) {
        throw new RuntimeException("List data attribute not supported for DataTag serialization!");
    }
}
