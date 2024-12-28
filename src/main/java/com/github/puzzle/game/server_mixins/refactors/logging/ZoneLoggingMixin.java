package com.github.puzzle.game.server_mixins.refactors.logging;

import finalforeach.cosmicreach.world.Zone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Zone.class)
public class ZoneLoggingMixin {
    @Shadow public String zoneId;
    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("CosmicReach | Zone");

    @Redirect(method = "validateSpawnPoint", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/util/logging/Logger;info(Ljava/lang/Object;)V"))
    private void log$info0(Object message) {
        LOGGER.info(message.toString());
    }

    @Redirect(method = "saveZone", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/util/logging/Logger;info(Ljava/lang/Object;)V"))
    private void log$info1(Object message) {
        LOGGER.info("Saved Zone with ID \"{}\"", zoneId);
    }

    @Redirect(method = "calculateSpawn", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/util/logging/Logger;info(Ljava/lang/Object;)V"))
    private void log$info2(Object message) {
        LOGGER.info(message.toString());
    }
}
