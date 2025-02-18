package io.github.puzzle.cosmic.api.util;

import io.github.puzzle.cosmic.util.ApiDeclaration;

@ApiDeclaration(api = IPuzzleIdentifier.class, impl = "Identifier")
public interface IPuzzleIdentifier {

    String _getNamespace();
    String _getName();

}
