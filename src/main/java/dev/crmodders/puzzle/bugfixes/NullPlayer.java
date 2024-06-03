package dev.crmodders.puzzle.bugfixes;

import finalforeach.cosmicreach.entities.Entity;
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

    @Shadow private static Player player;

    @Inject(method = "loadWorld(Lfinalforeach/cosmicreach/world/World;)V", at = @At("TAIL"))
    private void setLocalPlayer(World world, CallbackInfo ci) {
        if (player.getEntity() == null) {
            player.setEntity(new Entity());
            player.getZone(world).allEntities.add(player.getEntity());
            player.setPosition(0, 300, 0);
        }
    }
}
