package com.github.puzzle.game.mixins.refactors.blocks;

import com.github.puzzle.core.PuzzleRegistries;
import com.github.puzzle.game.block.IModBlock;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlocksMixin {

    @Shadow
    private String stringId;

    @Inject(method = "getName", at = @At("HEAD"), cancellable = true)
    private void setIModBlockName(CallbackInfoReturnable<String> cir){
        if(!PuzzleRegistries.BLOCKS.contains(Identifier.of(stringId))) return;
        IModBlock block = PuzzleRegistries.BLOCKS.get(Identifier.of(stringId));
        cir.setReturnValue(block.getName());
    }
}
