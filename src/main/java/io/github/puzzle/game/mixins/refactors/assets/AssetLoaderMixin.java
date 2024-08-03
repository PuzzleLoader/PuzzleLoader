package io.github.puzzle.game.mixins.refactors.assets;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import de.pottgames.tuningfork.SoundBuffer;
import io.github.puzzle.core.Identifier;
import io.github.puzzle.core.resources.PuzzleGameAssetLoader;
import finalforeach.cosmicreach.GameAssetLoader;
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
    public static FileHandle loadAsset(String fileName, boolean forceReload) {
        Identifier location = Identifier.fromString(fileName);
        if (!forceReload && ALL_ASSETS.containsKey(location.toString())) {
            return ALL_ASSETS.get(location.toString());
        }
        if("base".equals(location.namespace)) {
            fileName = location.name;
        }
        FileHandle handle = PuzzleGameAssetLoader.locateAsset(fileName);
        ALL_ASSETS.put(location.toString(), handle);
        return handle;
    }

    /**
     * @author nanobass
     * @reason uses libgdx asset manager, which can load assets better
     */
    @Overwrite
    public static SoundBuffer getSound(String fileName) {
        return PuzzleGameAssetLoader.LOADER.loadSync(fileName, SoundBuffer.class);
    }

    /**
     * @author nanobass
     * @reason uses libgdx asset manager, which can load assets better
     */
    @Overwrite
    public static Texture getTexture(String fileName) {
        return PuzzleGameAssetLoader.LOADER.loadSync(fileName, Texture.class);
    }
}