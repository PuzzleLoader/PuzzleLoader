package dev.crmodders.puzzle.mixins.entrypoint;

import dev.crmodders.puzzle.entrypoint.interfaces.ModInitializer;
import dev.crmodders.puzzle.mod.ModLocator;
import dev.crmodders.puzzle.utils.PuzzleEntrypointUtil;
import finalforeach.cosmicreach.BlockGame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockGame.class)
public class LoadInitModsMixin {

    @Inject(method = "create", at = @At("TAIL"))
    public void onInit(CallbackInfo ci) {
        PuzzleEntrypointUtil.invoke("init", ModInitializer.class, ModInitializer::onInit);
    }

}
