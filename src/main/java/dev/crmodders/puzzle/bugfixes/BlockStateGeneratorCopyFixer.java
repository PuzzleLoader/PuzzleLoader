package dev.crmodders.puzzle.bugfixes;

import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.blocks.BlockStateGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockStateGenerator.class)
public class BlockStateGeneratorCopyFixer {

    @Redirect(method = "generate", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/blocks/BlockState;copy(Z)Lfinalforeach/cosmicreach/blocks/BlockState;"))
    private BlockState fixedCopy(BlockState oldState, boolean initialize) {
        BlockState copy = oldState.copy(initialize);
        copy.generateSlabs = false;
        return copy;
    }

}