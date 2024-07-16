package dev.crmodders.puzzle.core.resources.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import dev.crmodders.puzzle.core.resources.Identifier;
import dev.crmodders.puzzle.core.resources.ResourceLocation;
import finalforeach.cosmicreach.io.SaveLocation;

import java.util.ArrayList;
import java.util.List;

public class VanillaAssetLocations {

    private static String[] internalFilesList = null;

    public static List<ResourceLocation> getInternalFiles(String folder, String extension) {
        List<ResourceLocation> files = new ArrayList<>();
        if(internalFilesList == null) internalFilesList = Gdx.files.internal("assets.txt").readString().split("\n");
        for(String internalFileName : internalFilesList) {
            if (internalFileName.startsWith(folder) && internalFileName.endsWith(extension) && Gdx.files.internal(internalFileName).exists()) {
                files.add(new ResourceLocation("base", internalFileName));
            }
        }
        return files;
    }

    public static FileHandle getModsFolder() {
        return Gdx.files.absolute(SaveLocation.getSaveFolderLocation() + "/mods");
    }

    public static List<ResourceLocation> getVanillaModFiles(String folder, String extension) {
        List<ResourceLocation> files = new ArrayList<>();
        for(FileHandle f : Gdx.files.absolute(SaveLocation.getSaveFolderLocation() + "/mods/assets/" + folder).list()) {
            String fileName = f.name();
            if (fileName.endsWith(extension)) {
                files.add(new ResourceLocation("base", fileName));
            }
        }
        return files;
    }

    private static ResourceLocation getLocation(String folder, String name, String extension) {
        Identifier identifier = Identifier.fromString(name);
        return new ResourceLocation(identifier.namespace, folder + "/" + identifier.name + "." + extension);
    }

    public static ResourceLocation getBlock(String blockName) {
        return getLocation("blocks", blockName, "json");
    }

    public static ResourceLocation getBlockTexture(String blockTextureName) {
        return getLocation("textures/blocks", blockTextureName, "png");
    }

    public static ResourceLocation getBlockModel(String blockModelName) {
        return getLocation("models/blocks", blockModelName, "json");
    }

    public static ResourceLocation getBlockEvents(String blockEventsName) {
        return getLocation("block_events", blockEventsName, "json");
    }

    public static ResourceLocation getTexture(String textureName) {
        return getLocation("textures", textureName, "png");
    }

    public static ResourceLocation getLanguage(String languageName) {
        return getLocation("lang", languageName, "json");
    }

    public static ResourceLocation getFontTexture(String fontTextureName) {
        return getLocation("lang/textures", fontTextureName, "png");
    }

    public static ResourceLocation getShader(String shaderName) {
        return getLocation("shaders", shaderName, "glsl");
    }
}