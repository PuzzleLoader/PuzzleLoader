package com.github.puzzle.game.mixins.refactors.logging;

import finalforeach.cosmicreach.rendering.shaders.GameShader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.PrintStream;

@Mixin(GameShader.class)
public class GameShaderMixin {
    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("CosmicReach | Shaders");

    @Redirect(method = "reload", at = @At(value = "INVOKE", target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V"), require = 0)
    private void print1(PrintStream instance, String x) {
        if(!x.isBlank()) LOGGER.info(x);
    }

    @Redirect(method = "reloadAllShaders", at = @At(value = "INVOKE", target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V"), require = 0)
    private static void print2(PrintStream instance, String x) {
        LOGGER.info(x);
    }
}