package dev.crmodders.puzzle.core.mixins.be;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import dev.crmodders.puzzle.core.entities.blocks.interfaces.IRenderable;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(InGame.class)
public class InGameMixin {

    @Shadow private static PerspectiveCamera rawWorldCamera;

    @Shadow private static Player player;

    @Shadow public static World world;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/BlockSelection;render(Lcom/badlogic/gdx/graphics/Camera;)V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private void renderPlayerZone(float partTick, CallbackInfo ci) {
        if(player.getZone(world) instanceof IRenderable renderable) {
            renderable.onRender(rawWorldCamera, partTick);
        }
    }

}