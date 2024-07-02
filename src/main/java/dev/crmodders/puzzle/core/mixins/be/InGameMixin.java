package dev.crmodders.puzzle.core.mixins.be;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.llamalad7.mixinextras.sugar.Local;
import dev.crmodders.puzzle.core.entities.blocks.interfaces.IRenderable;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.world.Sky;
import finalforeach.cosmicreach.world.Zone;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(InGame.class)
public class InGameMixin {

    @Shadow private static PerspectiveCamera rawWorldCamera;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/BlockSelection;render(Lcom/badlogic/gdx/graphics/Camera;)V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private void renderPlayerZone(CallbackInfo ci, Zone playerZone, Sky sky) {
        if(playerZone instanceof IRenderable renderable) {
            renderable.onRender(rawWorldCamera);
        }
    }

}