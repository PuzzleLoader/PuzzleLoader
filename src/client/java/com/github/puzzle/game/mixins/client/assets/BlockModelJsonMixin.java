package com.github.puzzle.game.mixins.client.assets;

import com.badlogic.gdx.files.FileHandle;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJson;
import finalforeach.cosmicreach.util.Identifier;
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

    @Redirect(method = "getInstance", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/GameAssetLoader;loadAsset(Lfinalforeach/cosmicreach/util/Identifier;)Lcom/badlogic/gdx/files/FileHandle;"))
    private static FileHandle getModelFromModID(Identifier location) {
        LOGGER.warn("using broken BlockModelJson.getInstance, this could possible indicate a broken mod");
        return GameAssetLoader.loadAsset(location);
    }
}