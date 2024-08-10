package com.github.puzzle.game.mixins.refactors.entrypoint;

import com.github.puzzle.game.engine.GameLoader;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.gamestates.GameState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameSingletons.class)
public class GameSingletonsMixin {

    @Inject(method = "postCreate", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/Threads;runOnMainThread(Ljava/lang/Runnable;)V", shift = At.Shift.BEFORE), cancellable = true)
    private static void postCreate(CallbackInfo ci) {
        GameState.switchToGameState(new GameLoader());
        ci.cancel();
    }

}
