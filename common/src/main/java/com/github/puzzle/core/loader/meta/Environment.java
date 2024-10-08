package com.github.puzzle.core.loader.meta;

public enum Environment {
    CLIENT("CLIENT"),
    SERVER("SERVER");

    public final String name;

    Environment(String sideName) {
        this.name = sideName;
    }
}
