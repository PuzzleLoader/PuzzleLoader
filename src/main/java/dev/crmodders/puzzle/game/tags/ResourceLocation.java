package dev.crmodders.puzzle.game.tags;

import com.badlogic.gdx.files.FileHandle;
import finalforeach.cosmicreach.GameAssetLoader;

/**
 * A Class that stores information about Resources
 * @author Mr-Zombii
 */
public class ResourceLocation extends Identifier {

    public ResourceLocation(String modId, String path) {
        super(modId, path);
    }

    public static ResourceLocation fromString(String id) {
        if (!id.contains(":")) id = "base:"+id;
        String[] splitId = id.split(":");
        return new ResourceLocation(splitId[0], splitId[1]);
    }

    /**
     * A method to load the resource mentioned in the constructor.
     * @param forceReload it replaces the currently loaded resource with a reload of it.
     */
    public FileHandle load(boolean forceReload) {
        return GameAssetLoader.loadAsset(toString(), forceReload);
    }

    /**
     * A method to load the current resource mentioned in the creation construction.
     */
    public FileHandle load() {
        return load(false);
    }

}
