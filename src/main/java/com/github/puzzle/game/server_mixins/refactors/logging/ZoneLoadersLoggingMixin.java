package com.github.puzzle.game.server_mixins.refactors.logging;

import com.badlogic.gdx.utils.ObjectMap;
import com.llamalad7.mixinextras.sugar.Local;
import finalforeach.cosmicreach.ZoneLoaders;
import finalforeach.cosmicreach.world.IZoneLoader;
import finalforeach.cosmicreach.world.Zone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ZoneLoaders.class)
public class ZoneLoadersLoggingMixin {
    @Shadow private ObjectMap<Zone, IZoneLoader> zoneLoaders;
    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("CosmicReach | Zone");

    @Redirect(method = "addZoneLoader", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/util/logging/Logger;info(Ljava/lang/Object;)V"))
    private void log$info0(Object message, @Local(argsOnly = true) Zone zone) {
        LOGGER.info("Added zone loader \"{}\" for zone \"{}\"", zoneLoaders.get(zone).getClass().getSimpleName(), zone.zoneId);
    }

}
