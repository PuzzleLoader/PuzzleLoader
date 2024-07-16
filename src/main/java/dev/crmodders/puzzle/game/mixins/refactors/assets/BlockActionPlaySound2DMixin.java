package dev.crmodders.puzzle.game.mixins.refactors.assets;

import de.pottgames.tuningfork.SoundBuffer;
import dev.crmodders.puzzle.core.Identifier;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.blockevents.actions.BlockActionPlaySound2D;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockActionPlaySound2D.class)
public class BlockActionPlaySound2DMixin {

    @Redirect(method = "act(Lfinalforeach/cosmicreach/blocks/BlockState;Lfinalforeach/cosmicreach/blockevents/BlockEventTrigger;Lfinalforeach/cosmicreach/world/Zone;)V", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/GameAssetLoader;getSound(Ljava/lang/String;)Lde/pottgames/tuningfork/SoundBuffer;"))
    private SoundBuffer redirect(String fileName) {
        String noFolder = fileName.replace("sounds/blocks/","");
        if (noFolder.contains(":")) {
            Identifier id = Identifier.fromString(noFolder);
            id.name = "sounds/blocks/" + id.name;
            return GameAssetLoader.getSound(id.toString());
        }
        return GameAssetLoader.getSound(fileName);
    }

}