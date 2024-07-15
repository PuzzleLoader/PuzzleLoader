package dev.crmodders.puzzle.core.game.mixins.entrypoint;

import dev.crmodders.puzzle.core.game.Globals;
import dev.crmodders.puzzle.core.loader.entrypoint.interfaces.PreInitModInitializer;
import dev.crmodders.puzzle.core.loader.launch.Piece;
import finalforeach.cosmicreach.lwjgl3.Lwjgl3Launcher;
import dev.crmodders.puzzle.core.loader.mod.ModLocator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Lwjgl3Launcher.class)
public class PreGameLaunchInit {

    @Inject(method = "main", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/lwjgl3/Lwjgl3Launcher;createApplication()Lcom/badlogic/gdx/backends/lwjgl3/Lwjgl3Application;"))
    private static void loadPreLaunch(String[] args, CallbackInfo ci) {
        ModLocator.getMods();
        ModLocator.AddBuiltinMods(Piece.provider);
        Globals.initRenderers();
        PreInitModInitializer.invokeEntrypoint();
    }

}
