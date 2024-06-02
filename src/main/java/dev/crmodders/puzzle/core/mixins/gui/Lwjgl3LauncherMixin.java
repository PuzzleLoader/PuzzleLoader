package dev.crmodders.puzzle.core.mixins.gui;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import dev.crmodders.flux.FluxSettings;
import finalforeach.cosmicreach.lwjgl3.Lwjgl3Launcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Lwjgl3Launcher.class)
public class Lwjgl3LauncherMixin {

	@Inject(method = "getDefaultConfiguration", at = @At("RETURN"), cancellable = true)
	private static void setupAntiAliasing(CallbackInfoReturnable<Lwjgl3ApplicationConfiguration> ci) {
		Lwjgl3ApplicationConfiguration config = ci.getReturnValue();
		config.setBackBufferConfig(8, 8, 8, 8, 24, 8, FluxSettings.AntiAliasing.getValue()); // 8, 8, 8, 8, 24, 8 are default values
		ci.setReturnValue(config);
	}

}