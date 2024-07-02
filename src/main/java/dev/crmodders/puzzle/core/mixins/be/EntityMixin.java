package dev.crmodders.puzzle.core.mixins.be;

import dev.crmodders.puzzle.core.entities.blocks.util.EntityAccessor;
import finalforeach.cosmicreach.entities.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public class EntityMixin implements EntityAccessor {


    @Shadow float sightRange;

    @Override
    public float getSightRange() {
        return sightRange;
    }
}
