package dev.crmodders.puzzle.game.mixins.refactors.assets;

import com.badlogic.gdx.files.FileHandle;
import dev.crmodders.puzzle.core.resources.Identifier;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockModelJson.class)
public class BlockModelJsonMixin {

    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("BlockModelJson");

    @Redirect(method = "getInstance", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/GameAssetLoader;loadAsset(Ljava/lang/String;)Lcom/badlogic/gdx/files/FileHandle;"))
    private static FileHandle getModelFromModID(String fileName) {
        LOGGER.warn("using broken BlockModelJson.getInstance, this could possible indicate a broken mod");
        String noFolder = fileName.replace("models/blocks/","");
        if (noFolder.contains(":")) {
            Identifier id = Identifier.fromString(noFolder);
            id.name = "models/blocks/" + id.name;
            return GameAssetLoader.loadAsset(id.toString());
        }
        return GameAssetLoader.loadAsset(fileName);
    }
}