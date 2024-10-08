package com.github.puzzle.util;

import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;

public class MutablePair<A, B> extends Pair<A, B> {

    public A a;
    public B b;

    public MutablePair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public A getLeft() {
        return a;
    }

    @Override
    public B getRight() {
        return b;
    }

    @Override
    public B setValue(B value) {
        this.b = value;
        return b;
    }
}
