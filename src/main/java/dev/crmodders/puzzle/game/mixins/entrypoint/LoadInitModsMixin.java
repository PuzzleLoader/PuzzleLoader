package dev.crmodders.puzzle.game.mixins.entrypoint;

import finalforeach.cosmicreach.BlockGame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockGame.class)
public class LoadInitModsMixin {

    @Inject(method = "create", at = @At("TAIL"))
    public void onInit(CallbackInfo ci) {
    }

}
