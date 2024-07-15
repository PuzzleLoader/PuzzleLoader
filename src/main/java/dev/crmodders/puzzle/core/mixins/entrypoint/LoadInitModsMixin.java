package dev.crmodders.puzzle.core.mixins.entrypoint;

//import dev.crmodders.flux.engine.GameLoader;
import dev.crmodders.puzzle.core.entrypoint.interfaces.ModInitializer;
import finalforeach.cosmicreach.BlockGame;
import finalforeach.cosmicreach.gamestates.GameState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockGame.class)
public class LoadInitModsMixin {

    @Inject(method = "create", at = @At("TAIL"))
    public void onInit(CallbackInfo ci) {
        ModInitializer.invokeEntrypoint();
    }

}
