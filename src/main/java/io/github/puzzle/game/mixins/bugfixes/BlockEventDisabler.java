package io.github.puzzle.game.mixins.bugfixes;

import finalforeach.cosmicreach.blockevents.BlockEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

@Mixin(BlockEvents.class)
public class BlockEventDisabler {

    @Shadow @Final public static HashMap<String, BlockEvents> INSTANCES;

    @Inject(method = "initBlockEvents", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/blockevents/BlockEvents;getInstance(Ljava/lang/String;)Lfinalforeach/cosmicreach/blockevents/BlockEvents;", shift = At.Shift.AFTER), cancellable = true)
    private static void initBlockEvents(CallbackInfo ci) {
        ci.cancel();
    }

}
