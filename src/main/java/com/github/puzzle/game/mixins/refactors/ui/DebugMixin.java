package com.github.puzzle.game.mixins.refactors.ui;

import finalforeach.cosmicreach.RuntimeInfo;
import finalforeach.cosmicreach.ui.debug.DebugInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(DebugInfo.class)
public class DebugMixin {
    /**
     * @author
     * @reason
     */
    @Overwrite
    static String lambda$static$0(){

        return RuntimeInfo.version + " | Puzzle Dev";

    }

}
