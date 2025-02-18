package io.github.puzzle.cosmic.api.block;

import io.github.puzzle.cosmic.api.util.IPuzzleIdentifier;
import io.github.puzzle.cosmic.util.ChangeType;

public interface IPuzzleBlock {

    IPuzzleBlockState getDefaultState();
    BlockStateMap getStates();

    IPuzzleIdentifier getIdentifier();
    String getName();

    interface BlockStateMap {

        IPuzzleBlockState get(String key);

    }

    @ChangeType("Block")
    Object as();

}
