package com.github.puzzle.game.mixins.refactors.assets;

import com.badlogic.gdx.files.FileHandle;
import com.github.puzzle.core.Identifier;
import com.github.puzzle.core.resources.PuzzleGameAssetLoader;
import com.github.puzzle.core.resources.ResourceLocation;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.rendering.shaders.GameShader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameShader.class)
public class GameShaderMixin {
    @Redirect(method = "loadShaderFile", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/GameAssetLoader;loadAsset(Ljava/lang/String;)Lcom/badlogic/gdx/files/FileHandle;"))
    FileHandle loadShaderFromMods(String fileName) {
        String noFolder = fileName.replace("shaders/", "");
        if (noFolder.contains(":")) {
            ResourceLocation resource = ResourceLocation.fromString(noFolder);
            resource.name = "shaders/" + resource.name;
            return resource.locate();
        }
        return PuzzleGameAssetLoader.locateAsset(fileName);
    }

    @Redirect(method = "loadShaderFile", at = @At(value = "INVOKE", target = "Ljava/lang/String;replaceAll(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"))
    String replaceAllInShaderName(String instance, String regex, String replacement) {
        ResourceLocation id = ResourceLocation.fromString(instance);
        return id.name.replaceAll(regex, replacement);
    }
}