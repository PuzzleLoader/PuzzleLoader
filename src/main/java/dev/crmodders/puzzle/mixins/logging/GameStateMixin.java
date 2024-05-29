package dev.crmodders.puzzle.mixins.logging;


import finalforeach.cosmicreach.gamestates.GameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.PrintStream;

@Mixin(GameState.class)
public class GameStateMixin {
    @Unique
    private static Logger logger = LogManager.getLogger("CR | Gamestate");

    @Redirect(method = "switchToGameState", at = @At(value = "INVOKE", target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V"))
    private static void printCapture(PrintStream instance, String x) {
        logger.info(x);
    }

}
