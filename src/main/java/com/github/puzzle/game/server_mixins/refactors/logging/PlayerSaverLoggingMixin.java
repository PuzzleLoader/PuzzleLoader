package com.github.puzzle.game.server_mixins.refactors.logging;

import finalforeach.cosmicreach.io.PlayerSaver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerSaver.class)
public class PlayerSaverLoggingMixin {
    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("CosmicReach | PlayerSaver");

    @Redirect(method = "savePlayer", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/util/logging/Logger;info(Ljava/lang/Object;)V"))
    private static void log$info0(Object message) {
        LOGGER.info(message.toString());
    }

    @Redirect(method = "loadPlayer", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/util/logging/Logger;info(Ljava/lang/Object;)V"))
    private static void log$info1(Object message) {
        LOGGER.info(message.toString());
    }
}
