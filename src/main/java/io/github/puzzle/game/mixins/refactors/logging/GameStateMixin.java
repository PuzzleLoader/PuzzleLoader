package io.github.puzzle.game.mixins.refactors.logging;

import io.github.puzzle.util.AnsiColours;
import finalforeach.cosmicreach.gamestates.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.PrintStream;

@Mixin(GameState.class)
public class GameStateMixin {
    @Shadow public static GameState currentGameState;
    @Unique private static final Logger LOGGER = LoggerFactory.getLogger("CosmicReach | GameState");

    @Redirect(method = "lambda$switchToGameState$0", at = @At(value = "INVOKE", target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V"), require = 0)
    private static void printCapture(PrintStream instance, String x, GameState gameState) {
        if(currentGameState == null) {
            LOGGER.info("Switched to GameState: " + AnsiColours.BLUE +"{}" + AnsiColours.WHITE, gameState.getClass().getSimpleName());
        } else {
            LOGGER.info("Switched from "+ AnsiColours.BLUE + "{}"+ AnsiColours.WHITE + " to " + AnsiColours.BLUE + "{}"+ AnsiColours.WHITE, currentGameState.getClass().getSimpleName(), gameState.getClass().getSimpleName());
        }
    }
}