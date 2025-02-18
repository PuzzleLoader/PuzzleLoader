package io.github.puzzle.cosmic.api.util;

import io.github.puzzle.cosmic.util.ChangeType;

public interface IPuzzleIdentifier {

    String getNamespace();
    String getName();

    @ChangeType("Identifier")
    Object as();

}
