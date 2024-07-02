package dev.crmodders.puzzle.core.entities.blocks.util;

import org.spongepowered.asm.mixin.Unique;

public interface EntityAccessor {

    @Unique
    float getSightRange();

}
