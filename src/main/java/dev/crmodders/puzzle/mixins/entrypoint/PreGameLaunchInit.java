package dev.crmodders.puzzle.mixins.entrypoint;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import dev.crmodders.puzzle.entrypoint.interfaces.ModPreInitializer;
import dev.crmodders.puzzle.utils.PuzzleEntrypointUtil;
import finalforeach.cosmicreach.lwjgl3.Lwjgl3Launcher;
import dev.crmodders.puzzle.mod.ModLocator;
import dev.crmodders.puzzle.entrypoint.interfaces.ModInitializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Lwjgl3Launcher.class)
public class PreGameLaunchInit {

    @Inject(method = "main", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/lwjgl3/Lwjgl3Launcher;createApplication()Lcom/badlogic/gdx/backends/lwjgl3/Lwjgl3Application;"))
    private static void loadPreLaunch(String[] args, CallbackInfo ci) {
        ModLocator.getMods();

        PuzzleEntrypointUtil.invoke("preInit", ModPreInitializer.class, ModPreInitializer::onPreInit);
    }

}
