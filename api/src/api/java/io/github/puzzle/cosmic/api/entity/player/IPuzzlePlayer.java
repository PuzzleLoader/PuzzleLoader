package io.github.puzzle.cosmic.api.entity.player;

import io.github.puzzle.cosmic.util.ChangeType;

public interface IPuzzlePlayer {

    @ChangeType("Player")
    Object as();

}
