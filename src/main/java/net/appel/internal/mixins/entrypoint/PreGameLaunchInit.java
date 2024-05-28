package net.appel.internal.mixins.entrypoint;

import finalforeach.cosmicreach.lwjgl3.Lwjgl3Launcher;
import dev.crmodders.puzzle.mod.ModLocator;
import dev.crmodders.puzzle.entrypoint.interfaces.ModInitializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Lwjgl3Launcher.class)
public class PreGameLaunchInit {

    @Inject(method = "main", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/lwjgl3/Lwjgl3Launcher;createApplication()Lcom/badlogic/gdx/backends/lwjgl3/Lwjgl3Application;"))
    private static void loadPreLaunch(String[] args, CallbackInfo ci) {
        ModLocator.getMods();

        ModLocator.LocatedMods.values().forEach(mod -> {
            ModLocator.invokeEntrypoint("main", ModInitializer.class, ModInitializer::onInit);
            ModLocator.invokeEntrypoint("preInit", ModInitializer.class, ModInitializer::onInit);
        });
    }

}
