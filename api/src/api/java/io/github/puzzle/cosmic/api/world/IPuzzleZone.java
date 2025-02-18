package io.github.puzzle.cosmic.api.world;

import io.github.puzzle.cosmic.api.util.IPuzzleIdentifier;
import io.github.puzzle.cosmic.util.ApiDeclaration;

@ApiDeclaration(api = IPuzzleZone.class, impl = "Zone")
public interface IPuzzleZone {

    IPuzzleIdentifier getId();

}
