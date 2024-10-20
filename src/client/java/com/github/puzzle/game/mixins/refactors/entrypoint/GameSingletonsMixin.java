package com.github.puzzle.game.mixins.refactors.entrypoint;

import com.github.puzzle.game.engine.ClientGameLoader;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.gamestates.GameState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GameSingletons.class)
public class GameSingletonsMixin {

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static void postCreate() {
        GameState.switchToGameState(new ClientGameLoader());
    }

}
