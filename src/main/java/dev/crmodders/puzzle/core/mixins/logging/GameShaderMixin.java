package dev.crmodders.puzzle.core.mixins.logging;


import finalforeach.cosmicreach.rendering.shaders.GameShader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.PrintStream;

@Mixin(GameShader.class)
public class GameShaderMixin {

    @Unique
    private static Logger logger = LogManager.getLogger("CR | Shaders");

    @Redirect(method = "reload", at = @At(value = "INVOKE", target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V"))
    private void print1(PrintStream instance, String x) {
        logger.info(x);
    }

    @Redirect(method = "reloadAllShaders", at = @At(value = "INVOKE", target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V"))
    private static void print2(PrintStream instance, String x) {
        logger.info(x);
    }

}
