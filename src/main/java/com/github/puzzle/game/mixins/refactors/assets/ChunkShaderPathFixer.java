package com.github.puzzle.game.mixins.refactors.assets;

import com.badlogic.gdx.files.FileHandle;
import com.github.puzzle.core.resources.PuzzleGameAssetLoader;
import finalforeach.cosmicreach.rendering.shaders.ChunkShader;
import finalforeach.cosmicreach.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChunkShader.class)
public class ChunkShaderPathFixer {
    @Redirect(method = "addToAllBlocksTexture",at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/GameAssetLoader;loadAsset(Lfinalforeach/cosmicreach/util/Identifier;)Lcom/badlogic/gdx/files/FileHandle;"))
    private static FileHandle loadModdedBlockTextures(Identifier location){
        if (!location.getName().startsWith("textures/blocks/")) location = Identifier.of(location.getNamespace(), "shaders/" + location.getName());
        return PuzzleGameAssetLoader.locateAsset(location);
    }
}