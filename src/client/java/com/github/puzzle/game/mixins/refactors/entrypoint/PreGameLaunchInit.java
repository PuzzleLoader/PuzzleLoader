package com.github.puzzle.game.mixins.refactors.entrypoint;

import com.github.puzzle.core.loader.launch.Piece;
import com.github.puzzle.core.loader.launch.provider.mod.entrypoint.impls.ClientPreModInitializer;
import com.github.puzzle.game.ClientGlobals;
import finalforeach.cosmicreach.lwjgl3.Lwjgl3Launcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Lwjgl3Launcher.class)
public class PreGameLaunchInit {
    @Inject(method = "main", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/lwjgl3/Lwjgl3Launcher;createApplication()Lcom/badlogic/gdx/backends/lwjgl3/Lwjgl3Application;", shift = At.Shift.BEFORE))
    private static void loadPreLaunch(String[] args, CallbackInfo ci) {
        Piece.provider.addBuiltinMods();
        ClientPreModInitializer.invokeEntrypoint();
    }
}
