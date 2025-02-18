package io.github.puzzle.cosmic.api.block;

import io.github.puzzle.cosmic.api.item.IPuzzleItem;
import io.github.puzzle.cosmic.api.util.IPuzzleIdentifier;
import io.github.puzzle.cosmic.util.ChangeType;

public interface IPuzzleBlockState {

    IPuzzleBlock getBlock();
    IPuzzleItem getAsItem();

    IPuzzleIdentifier getBlockID();
    String getSaveKey();

    @ChangeType("BlockState")
    Object as();

}
