package com.github.puzzle.game.server_mixins.refactors.logging;

import finalforeach.cosmicreach.io.ChunkSaver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChunkSaver.class)
public class ChunkSaverLoggingMixin {
    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("CosmicReach | ChunkSaver");

    @Redirect(method = "saveWorld", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/util/logging/Logger;info(Ljava/lang/Object;)V"))
    private static void log$info(Object message) {
        LOGGER.info(message.toString());
    }

}
