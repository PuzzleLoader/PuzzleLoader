package io.github.puzzle.cosmic.impl.mixin.world;

import finalforeach.cosmicreach.util.Identifier;
import finalforeach.cosmicreach.world.Zone;
import io.github.puzzle.cosmic.api.util.IPuzzleIdentifier;
import io.github.puzzle.cosmic.api.world.IPuzzleZone;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Zone.class)
public class ZoneMixin implements IPuzzleZone {

    @Shadow public String zoneId;

    @Override
    public IPuzzleIdentifier getId() {
        return IPuzzleIdentifier.as(Identifier.of(zoneId));
    }
}
