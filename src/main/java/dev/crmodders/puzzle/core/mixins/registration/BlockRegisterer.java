package dev.crmodders.puzzle.core.mixins.registration;

import finalforeach.cosmicreach.blocks.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockRegisterer {

    @Inject(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/String;split(Ljava/lang/String;)[Ljava/lang/String;"
            ),
            cancellable = true
    )
    private static void dummy0(CallbackInfo ci) {
        ci.cancel();
    }

}