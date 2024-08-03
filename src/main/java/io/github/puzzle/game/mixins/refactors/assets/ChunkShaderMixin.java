package io.github.puzzle.game.mixins.refactors.assets;

import com.badlogic.gdx.files.FileHandle;
import io.github.puzzle.core.Identifier;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.rendering.shaders.ChunkShader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChunkShader.class)
public class ChunkShaderMixin {
    @Redirect(method = "addToAllBlocksTexture",at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/GameAssetLoader;loadAsset(Ljava/lang/String;)Lcom/badlogic/gdx/files/FileHandle;"))
    private static FileHandle loadModdedBlockTextures(String fileName){
        String noFolder = fileName.replace("textures/blocks/","");
        if (noFolder.contains(":")) {
            Identifier id = Identifier.fromString(noFolder);
            id.name = "textures/blocks/" + id.name;
            return GameAssetLoader.loadAsset(id.toString());
        }
        return GameAssetLoader.loadAsset(fileName);
    }
}