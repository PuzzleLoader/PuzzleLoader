package dev.crmodders.puzzle.core.block_entities.util;

import org.spongepowered.asm.mixin.Unique;

public interface EntityAccessor {

    @Unique
    float getSightRange();

}
