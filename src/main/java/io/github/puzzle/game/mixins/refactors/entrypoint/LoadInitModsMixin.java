package io.github.puzzle.game.mixins.refactors.entrypoint;

import finalforeach.cosmicreach.BlockGame;
import finalforeach.cosmicreach.gamestates.GameState;
import io.github.puzzle.game.engine.GameLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockGame.class)
public class LoadInitModsMixin {
    @Shadow public static boolean gameStarted;

    @Inject(method = "create", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/ClientSingletons;create()V", shift = At.Shift.AFTER), cancellable = true)
    public void onInit(CallbackInfo ci) {
        GameState.switchToGameState(new GameLoader());
        ci.cancel();
        gameStarted = true;
    }
}
