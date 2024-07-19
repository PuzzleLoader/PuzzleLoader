package dev.crmodders.puzzle.game.mixins.refactors.assets;

import com.badlogic.gdx.files.FileHandle;
import dev.crmodders.puzzle.core.Identifier;
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
            Identifier id = Identifier.fromString(noFolder);
            id.name = "shaders/" + id.name;
            return GameAssetLoader.loadAsset(id.toString());
        }
        return GameAssetLoader.loadAsset(fileName);
    }

    @Redirect(method = "loadShaderFile", at = @At(value = "INVOKE", target = "Ljava/lang/String;replaceAll(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"))
    String replaceAllInShaderName(String instance, String regex, String replacement) {
        Identifier id = Identifier.fromString(instance);
        return id.name.replaceAll(regex, replacement);
    }
}