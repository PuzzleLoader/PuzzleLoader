package com.github.puzzle.core.resources;

import com.badlogic.gdx.files.FileHandle;
import com.github.puzzle.core.Identifier;

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

    public String toPath() {
        return "assets/" + namespace + "/" + name;
    }

    public FileHandle locate() {
        return PuzzleGameAssetLoader.locateAsset(this);
    }

}