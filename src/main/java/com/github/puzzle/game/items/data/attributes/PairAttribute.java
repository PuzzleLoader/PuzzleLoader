package com.github.puzzle.game.items.data.attributes;

import com.github.puzzle.core.loader.launch.Piece;
import com.github.puzzle.game.items.data.DataTag;
import com.github.puzzle.util.MutablePair;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import finalforeach.cosmicreach.savelib.crbin.CRBinDeserializer;
import finalforeach.cosmicreach.savelib.crbin.CRBinSerializer;
import finalforeach.cosmicreach.savelib.crbin.ICRBinSerializable;

public class PairAttribute<A extends ICRBinSerializable, B extends ICRBinSerializable> implements DataTag.DataTagAttribute<Pair<A, B>> {

    Pair<A, B> pair;

    public PairAttribute() {}

    public PairAttribute(A a, B b) {
        this.pair = new MutablePair<>(a, b);
    }

    public PairAttribute(Pair<A, B> pair) {
        this.pair = pair;
    }

    @Override
    public void setValue(Pair<A, B> value) {
        this.pair = value;
    }

    @Override
    public Pair<A, B> getValue() {
        return pair;
    }

    @Override
    public DataTag.DataTagAttribute<Pair<A, B>> copyAndSetValue(Pair<A, B> value) {
        return new PairAttribute<>(value);
    }

    @Override
    public String getFormattedString() {
        return "( " + pair.getLeft().toString() + " " + pair.getRight().toString() + "  )";
    }

    @Override
    public String toString() {
        return getFormattedString();
    }

    @Override
    public void read(CRBinDeserializer crBinDeserializer) {
        try {
            Class<A> leftType = (Class<A>) Piece.classLoader.findClass(crBinDeserializer.readString("leftType"));
            A left = crBinDeserializer.readObj("left", leftType);

            Class<B> rightType = (Class<B>) Piece.classLoader.findClass(crBinDeserializer.readString("rightType"));
            B right = crBinDeserializer.readObj("right", rightType);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(CRBinSerializer crBinSerializer) {
        crBinSerializer.writeString("leftType", getValue().getLeft().getClass().getName());
        crBinSerializer.writeObj("left", getValue().getLeft());
        crBinSerializer.writeString("rightType", getValue().getRight().getClass().getName());
        crBinSerializer.writeObj("right", getValue().getRight());
    }
}
