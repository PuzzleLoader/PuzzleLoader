package com.github.puzzle.game.mixins.refactors.entrypoint;

import com.github.puzzle.game.engine.GameLoader;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.gamestates.GameState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameSingletons.class)
public class GameSingletonsMixin {

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static void postCreate() {
        GameState.switchToGameState(new GameLoader());
    }

}
