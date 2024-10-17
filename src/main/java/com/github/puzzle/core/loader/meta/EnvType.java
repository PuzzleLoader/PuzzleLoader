package com.github.puzzle.core.loader.meta;

public enum EnvType {
    CLIENT("CLIENT"),
    SERVER("SERVER");

    public final String name;

    EnvType(String sideName) {
        this.name = sideName;
    }
}
