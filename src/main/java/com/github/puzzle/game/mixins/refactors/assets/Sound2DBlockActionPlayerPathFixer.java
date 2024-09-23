package com.github.puzzle.game.mixins.refactors.assets;

import de.pottgames.tuningfork.SoundBuffer;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.blockevents.actions.BlockActionPlaySound2D;
import finalforeach.cosmicreach.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockActionPlaySound2D.class)
public class Sound2DBlockActionPlayerPathFixer {
    @Redirect(method = "act(Lfinalforeach/cosmicreach/blocks/BlockState;Lfinalforeach/cosmicreach/blockevents/BlockEventTrigger;Lfinalforeach/cosmicreach/world/Zone;)V",at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/GameAssetLoader;getSound(Lfinalforeach/cosmicreach/util/Identifier;)Lde/pottgames/tuningfork/SoundBuffer;"))
    private SoundBuffer loadModdedBlockTextures(Identifier location){
        if (!location.getName().startsWith("sounds/blocks/")) location = Identifier.of(location.getNamespace(), "sounds/blocks/" + location.getName());
        return GameAssetLoader.getSound(location);
    }
}