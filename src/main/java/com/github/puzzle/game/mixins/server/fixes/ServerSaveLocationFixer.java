package com.github.puzzle.game.mixins.server.fixes;

import com.github.puzzle.game.ServerGlobals;
import finalforeach.cosmicreach.io.SaveLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SaveLocation.class)
public class ServerSaveLocationFixer {

    @Shadow public static String saveLocationOverride;

    /**
     * @author Mr Zombii
     * @reason Fix stupid save location.
     */
    @Overwrite
    public static String getSaveFolderLocation() {
        if (saveLocationOverride != null) {
            return saveLocationOverride;
        } else {
            saveLocationOverride = ServerGlobals.getServerLocation().getAbsolutePath();
            return ServerGlobals.getServerLocation().getAbsolutePath();
        }
    }

}
