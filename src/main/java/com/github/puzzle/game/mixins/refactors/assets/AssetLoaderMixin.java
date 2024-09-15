package com.github.puzzle.game.mixins.refactors.assets;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import finalforeach.cosmicreach.util.Identifier;
import com.github.puzzle.core.resources.PuzzleGameAssetLoader;
import de.pottgames.tuningfork.SoundBuffer;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.GameSingletons;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;

@Mixin(GameAssetLoader.class)
public class AssetLoaderMixin {
    @Shadow @Final public static HashMap<String, FileHandle> ALL_ASSETS;

    /**
     * @author written by replet, rewritten by Mr Zombii, replaced by nanobass
     * @reason Improves asset loading
     **/
    @Overwrite
    public static FileHandle loadAsset(Identifier location) {
        if (ALL_ASSETS.containsKey(location.toString())) {
            return ALL_ASSETS.get(location.toString());
        }
        FileHandle handle = PuzzleGameAssetLoader.locateAsset(location);
        ALL_ASSETS.put(location.toString(), handle);
        return handle;
    }
    /**
     * @author nanobass
     * @reason uses libgdx asset manager, which can load assets better
     */
    @Overwrite
    public static SoundBuffer getSound(String fileName) {
        return GameSingletons.soundManager.loadSound(PuzzleGameAssetLoader.locateAsset(fileName));
//        return PuzzleGameAssetLoader.LOADER.loadSync(fileName, SoundBuffer.class);
    }

    /**
     * @author nanobass
     * @reason uses libgdx asset manager, which can load assets better
     */
    @Overwrite
    public static Texture getTexture(String fileName) {
        return new Texture(PuzzleGameAssetLoader.locateAsset(fileName));
//        return PuzzleGameAssetLoader.LOADER.loadSync(fileName, Texture.class);
    }
}