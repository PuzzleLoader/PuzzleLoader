package dev.crmodders.puzzle.core.mixins.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.io.SaveLocation;
import dev.crmodders.puzzle.game.tags.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;

@Mixin(GameAssetLoader.class)
public class AssetLoaderMixin {
    @Unique
    private static final Logger logger = LogManager.getLogger("Puzzle | AssetLoader");

    @Shadow @Final public static HashMap<String, FileHandle> ALL_ASSETS;

    /**
     * @author written by Mr Zombii
     * @reason Improves asset loading
     **/
    @Inject(method = "loadAsset(Ljava/lang/String;Z)Lcom/badlogic/gdx/files/FileHandle;", at = @At("HEAD"), cancellable = true)
    private static void loadAsset(String fileName, boolean forceReload, CallbackInfoReturnable<FileHandle> cir) {
        Identifier location = Identifier.fromString(fileName);
        if (!forceReload && ALL_ASSETS.containsKey(location.toString())) {
            cir.setReturnValue(ALL_ASSETS.get(location.toString()));
            return;
        }
        if("base".equals(location.namespace)) {
            fileName = location.name;
        }

        FileHandle classpathLocationFile = Gdx.files.classpath("assets/%s/%s".formatted(location.namespace, location.name));
        if (classpathLocationFile.exists()) {
            logger.info("Loading {} from the Classpath", fileName);
            ALL_ASSETS.put(fileName, classpathLocationFile);
            cir.setReturnValue(classpathLocationFile);
            return;
        }

        FileHandle modLocationFile = Gdx.files.absolute(SaveLocation.getSaveFolderLocation() + "/mods/assets/" + fileName);
        if (modLocationFile.exists()) {
            logger.info("Loading {} from DataMods", fileName);
            ALL_ASSETS.put(fileName, modLocationFile);
            cir.setReturnValue(modLocationFile);
            return;
        }

        FileHandle vanillaLocationFile = Gdx.files.internal(location.name);
        if (vanillaLocationFile.exists()) {
            logger.info("Loading {} from Cosmic Reach Jar", fileName);
            ALL_ASSETS.put(fileName, vanillaLocationFile);
            cir.setReturnValue(vanillaLocationFile);
            return;
        }

        logger.error("Cannot find the resource {}, ResourceId: {}", fileName, location);
        cir.setReturnValue(null);
    }

    @Redirect(method = "getSound", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/GameAssetLoader;loadAsset(Ljava/lang/String;)Lcom/badlogic/gdx/files/FileHandle;"))
    private static FileHandle getSound(String fileName) {
        String noFolder = fileName.replace("sounds/blocks/","");
        if (noFolder.contains(":")) {
            Identifier id = Identifier.fromString(noFolder);
            id.name = "sounds/blocks/" + id.name;
            return GameAssetLoader.loadAsset(id.toString());
        }
        return GameAssetLoader.loadAsset(fileName);
    }

}