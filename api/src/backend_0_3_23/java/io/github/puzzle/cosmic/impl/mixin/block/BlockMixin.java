package io.github.puzzle.cosmic.impl.mixin.block;

import finalforeach.cosmicreach.blocks.Block;
import io.github.puzzle.cosmic.api.block.IPuzzleBlock;
import io.github.puzzle.cosmic.api.block.IPuzzleBlockState;
import io.github.puzzle.cosmic.impl.util.Identifier;
import io.github.puzzle.cosmic.api.util.IPuzzleIdentifier;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Block.class)
public class BlockMixin implements IPuzzleBlock {

    Block block = (Block) (Object) this;

    @Override
    public IPuzzleBlockState getDefaultState() {
        return (IPuzzleBlockState) block.getDefaultBlockState();
    }

    @Override
    public BlockStateMap getStates() {
        return new BlockStateMap() {
            @Override
            public IPuzzleBlockState get(String key) {
                return (IPuzzleBlockState) block.blockStates.get(key);
            }
        };
    }

    @Override
    public IPuzzleIdentifier getIdentifier() {
        return Identifier.of(block.getStringId());
    }

    @Override
    public String getName() {
        return block.getName();
    }

    @Override
    public Block as() {
        return block;
    }

}
