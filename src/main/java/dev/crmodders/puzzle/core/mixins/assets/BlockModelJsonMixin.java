package dev.crmodders.puzzle.core.mixins.assets;

import com.badlogic.gdx.files.FileHandle;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJson;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockModelJson.class)
public class BlockModelJsonMixin {
    @Redirect(method = "getInstance", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/GameAssetLoader;loadAsset(Ljava/lang/String;)Lcom/badlogic/gdx/files/FileHandle;"))
    private static FileHandle getModelFromModID(String fileName) {
        String noFolder = fileName.replace("models/blocks/","");
        if (noFolder.contains(":")) {
            Identifier id = Identifier.fromString(noFolder);
            id.name = "models/blocks/" + id.name;
            return GameAssetLoader.loadAsset(id.toString());
        }
        return GameAssetLoader.loadAsset(fileName);
    }
}