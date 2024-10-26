package com.github.puzzle.game.tags.data.attributes;

import com.github.puzzle.game.tags.data.DataTag;
import finalforeach.cosmicreach.savelib.crbin.CRBinDeserializer;
import finalforeach.cosmicreach.savelib.crbin.CRBinSerializer;
import finalforeach.cosmicreach.savelib.crbin.ICRBinSerializable;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListDataAttribute<T extends ICRBinSerializable> implements DataTag.DataTagAttribute<List<T>> {

    List<T> data;

    public ListDataAttribute() {}

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
    public String toString() {
        return getFormattedString();
    }

    @Override
    public void read(CRBinDeserializer crBinDeserializer) {
        int arraySize = crBinDeserializer.readInt("arraySize", 0);
        data = new ArrayList<>();
        for (int i = 0; i < arraySize; i++) {
            PairAttribute<StringDataAttribute, T> pairAttribute = crBinDeserializer.readObj("object_" + i, PairAttribute.class);
            T value = pairAttribute.pair.getRight();

            data.add(value);
        }
    }

    @Override
    public void write(CRBinSerializer crBinSerializer) {
        crBinSerializer.writeInt("arraySize", data.size());
        for (int i = 0; i < data.size(); i++) {
            MutablePair<StringDataAttribute, T> pair = new MutablePair<>(new StringDataAttribute(data.get(i).getClass().getName()), data.get(i));
            crBinSerializer.writeObj("object_" + i, new PairAttribute<>(pair));
        }
    }
}