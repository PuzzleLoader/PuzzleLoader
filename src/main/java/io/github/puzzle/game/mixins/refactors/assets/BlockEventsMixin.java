package io.github.puzzle.game.mixins.refactors.assets;

import com.badlogic.gdx.files.FileHandle;
import io.github.puzzle.core.Identifier;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.blockevents.BlockEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockEvents.class)
public class BlockEventsMixin {
    @Redirect(method = "getInstance(Ljava/lang/String;Ljava/lang/String;)Lfinalforeach/cosmicreach/blockevents/BlockEvents;", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/GameAssetLoader;loadAsset(Ljava/lang/String;)Lcom/badlogic/gdx/files/FileHandle;"))
    private static FileHandle getModelFromModID(String fileName) {
        String noFolder = fileName.replace("block_events/","");
        if (noFolder.contains(":")) {
            Identifier id = Identifier.fromString(noFolder);
            id.name = "block_events/" + id.name;
            return GameAssetLoader.loadAsset(id.toString());
        }
        return GameAssetLoader.loadAsset(fileName);
    }
}