package io.github.puzzle.cosmic.impl.mixin.block;

import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.util.Identifier;
import io.github.puzzle.cosmic.api.block.IPuzzleBlock;
import io.github.puzzle.cosmic.api.block.IPuzzleBlockState;
import io.github.puzzle.cosmic.api.item.IPuzzleItem;
import io.github.puzzle.cosmic.api.util.IPuzzleIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockState.class)
public abstract class BlockStateMixin implements IPuzzleBlockState {

    @Unique
    BlockState puzzleLoader$state = IPuzzleBlockState.as(this);

    @Override
    public IPuzzleBlock _getBlock() {
        return (IPuzzleBlock) puzzleLoader$state.getBlock();
    }

    @Override
    public IPuzzleItem _getAsItem() {
        return (IPuzzleItem) puzzleLoader$state.getItem();
    }

    @Override
    public IPuzzleIdentifier _getBlockID() {
        return (IPuzzleIdentifier) Identifier.of(puzzleLoader$state.getBlockId());
    }

    @Override
    public String _getSaveKey() {
        return puzzleLoader$state.getSaveKey();
    }
}
