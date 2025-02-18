package io.github.puzzle.cosmic.api.world;

import io.github.puzzle.cosmic.util.ChangeType;

public interface IPuzzleChunk {

    @ChangeType("Chunk")
    Object as();

}
