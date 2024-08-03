package io.github.puzzle.game.mixins.bugfixes;

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
        // deprecated
        copy.generateSlabs = false;

        // does the same thing
        /*List<String> stateGenerators = Arrays.asList(copy.stateGenerators);
        stateGenerators.remove("base:slabs_all");
        copy.stateGenerators = stateGenerators.toArray(new String[0]);*/

        return copy;
    }
}