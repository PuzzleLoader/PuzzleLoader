package io.github.puzzle.cosmic.util;

public interface IPuzzleIdentifier {

    String getNamespace();
    String getName();

    default Object as() {
        return this;
    }

}
