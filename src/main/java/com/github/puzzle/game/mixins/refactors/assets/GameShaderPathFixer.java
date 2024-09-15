package com.github.puzzle.game.mixins.refactors.assets;

import com.badlogic.gdx.files.FileHandle;
import com.github.puzzle.core.resources.PuzzleGameAssetLoader;
import com.github.puzzle.game.util.Reflection;
import finalforeach.cosmicreach.rendering.shaders.GameShader;
import finalforeach.cosmicreach.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameShader.class)
public class GameShaderPathFixer {

    @Redirect(method = "loadShaderFile", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/GameAssetLoader;loadAsset(Lfinalforeach/cosmicreach/util/Identifier;)Lcom/badlogic/gdx/files/FileHandle;"))
    FileHandle loadShaderFromMods(Identifier location) {
        if (!location.getName().startsWith("shaders/")) location = Identifier.of(location.getNamespace(), "shaders/" + location.getName());
        return PuzzleGameAssetLoader.locateAsset(location);
    }

    @Redirect(method = "loadShaderFile", at = @At(value = "INVOKE", target = "Ljava/lang/String;replaceAll(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"))
    String replaceAllInShaderName(String instance, String regex, String replacement) {
        Identifier id = Identifier.of(instance);
        return id.getName().replaceAll(regex, replacement);
    }

}
