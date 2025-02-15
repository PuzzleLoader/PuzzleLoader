package io.github.puzzle.cosmic.api.item;

import io.github.puzzle.cosmic.api.block.IPuzzleBlockPosition;
import io.github.puzzle.cosmic.api.entity.player.IPuzzlePlayer;
import io.github.puzzle.cosmic.api.util.IPuzzleIdentifier;
import io.github.puzzle.cosmic.util.ChangeType;

public interface IPuzzleItem {

    IPuzzleIdentifier getIdentifier();

    default void use(IPuzzleItemSlot slot, IPuzzlePlayer player, IPuzzleBlockPosition placeTarget, IPuzzleBlockPosition breakTarget) {

    }

    @ChangeType("Item")
    Object as();

}
