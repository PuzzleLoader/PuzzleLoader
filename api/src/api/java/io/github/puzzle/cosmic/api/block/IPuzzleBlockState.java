package io.github.puzzle.cosmic.api.block;

import io.github.puzzle.cosmic.api.item.IPuzzleItem;
import io.github.puzzle.cosmic.api.util.IPuzzleIdentifier;
import io.github.puzzle.cosmic.util.ApiDeclaration;

@ApiDeclaration(api = IPuzzleBlockState.class, impl = "BlockState")
public interface IPuzzleBlockState {

    IPuzzleBlock _getBlock();
    IPuzzleItem _getAsItem();

    IPuzzleIdentifier _getBlockID();
    String _getSaveKey();

}
