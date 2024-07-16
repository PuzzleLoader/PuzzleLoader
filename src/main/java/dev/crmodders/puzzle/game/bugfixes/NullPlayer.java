package dev.crmodders.puzzle.game.bugfixes;

import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGame.class)
public class NullPlayer {

    @Shadow private static Player localPlayer;

    @Inject(method = "loadWorld(Lfinalforeach/cosmicreach/world/World;)V", at = @At("TAIL"))
    private void setLocalPlayer(World world, CallbackInfo ci) {
//        if (localPlayer.getEntity() == null) {
//            localPlayer.setEntity(new Entity());
//            localPlayer.getZone(world).allEntities.add(localPlayer.getEntity());
//            localPlayer.setPosition(0, 300, 0);
//        }
    }
}
