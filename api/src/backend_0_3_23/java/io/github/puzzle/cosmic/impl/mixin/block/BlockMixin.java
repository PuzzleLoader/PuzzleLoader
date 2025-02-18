package io.github.puzzle.cosmic.impl.mixin.block;

import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.util.Identifier;
import io.github.puzzle.cosmic.api.block.IPuzzleBlock;
import io.github.puzzle.cosmic.api.block.IPuzzleBlockState;
import io.github.puzzle.cosmic.api.util.IPuzzleIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Block.class)
public class BlockMixin implements IPuzzleBlock {

    @Unique
    Block puzzleLoader$block = IPuzzleBlock.as(this);

    @Override
    public IPuzzleBlockState _getDefaultState() {
        return (IPuzzleBlockState) puzzleLoader$block.getDefaultBlockState();
    }

    @Override
    public BlockStateMap _getStates() {
        return new BlockStateMap() {
            @Override
            public IPuzzleBlockState get(String key) {
                return (IPuzzleBlockState) puzzleLoader$block.blockStates.get(key);
            }
        };
    }

    @Override
    public IPuzzleIdentifier _getIdentifier() {
        return IPuzzleIdentifier.as(Identifier.of(puzzleLoader$block.getStringId()));
    }

    @Override
    public String _getName() {
        return puzzleLoader$block.getName();
    }

    @Override
    public Block as() {
        return puzzleLoader$block;
    }

}
