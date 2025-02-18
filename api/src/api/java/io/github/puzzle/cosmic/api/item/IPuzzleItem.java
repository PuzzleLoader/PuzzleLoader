package io.github.puzzle.cosmic.api.item;

import io.github.puzzle.cosmic.api.block.IPuzzleBlockPosition;
import io.github.puzzle.cosmic.api.entity.player.IPuzzlePlayer;
import io.github.puzzle.cosmic.api.util.IPuzzleIdentifier;
import io.github.puzzle.cosmic.util.ApiDeclaration;

@ApiDeclaration(api = IPuzzleItem.class, impl = "Item")
public interface IPuzzleItem {

    IPuzzleIdentifier _getIdentifier();

    default void _use(IPuzzleItemSlot slot, IPuzzlePlayer player, IPuzzleBlockPosition placeTarget, IPuzzleBlockPosition breakTarget) {

    }

}
