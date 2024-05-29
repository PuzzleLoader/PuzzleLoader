package dev.crmodders.puzzle.mixins.window;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Lwjgl3WindowConfiguration.class)
public class TitleMixin {

    @Shadow String title;

    @Inject(method = "setTitle", at = @At("HEAD"), cancellable = true)
    private void setTitle(String title, CallbackInfo ci) {
        this.title = "Puzzle Loader | " + title;
        ci.cancel();
    }

}
