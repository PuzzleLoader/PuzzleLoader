package com.github.puzzle.core.util;

import com.badlogic.gdx.files.FileHandle;
import com.github.puzzle.game.resources.PuzzleGameAssetLoader;

/**
 * A Class that stores information about Resources
 * @author Mr-Zombii
 */
public class ResourceLocation extends Identifier {

    public ResourceLocation(String modId, String path) {
        super(modId, path);
    }

    public static ResourceLocation fromVanilla(finalforeach.cosmicreach.util.ResourceLocation location) {
        return new ResourceLocation(location.getNamespace(), location.getName());
    }

    public static ResourceLocation fromString(String id) {
        if (!id.contains(":")) id = "base:"+id;
        String[] splitId = id.split(":");
        return new ResourceLocation(splitId[0], splitId[1]);
    }

    public String toPath() {
        return "assets/" + namespace + "/" + name;
    }

    public FileHandle locate() {
        return PuzzleGameAssetLoader.locateAsset(this);
    }

}