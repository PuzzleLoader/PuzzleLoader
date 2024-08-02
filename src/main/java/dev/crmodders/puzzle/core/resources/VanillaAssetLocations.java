package dev.crmodders.puzzle.core.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import finalforeach.cosmicreach.io.SaveLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VanillaAssetLocations {
    private static String[] internalFilesList = null;

    public static @NotNull List<ResourceLocation> getInternalFiles(String folder, String extension) {
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

    public static @NotNull List<ResourceLocation> getVanillaModFiles(String folder, String extension) {
        List<ResourceLocation> files = new ArrayList<>();
        for(FileHandle f : Gdx.files.absolute(SaveLocation.getSaveFolderLocation() + "/mods/assets/" + folder).list()) {
            String fileName = f.name();
            if (fileName.endsWith(extension)) {
                files.add(new ResourceLocation("base", fileName));
            }
        }
        return files;
    }

    private static @NotNull ResourceLocation getLocation(String folder, String name, String extension) {
        ResourceLocation identifier = ResourceLocation.fromString(name);
        return new ResourceLocation(identifier.namespace, folder + "/" + identifier.name + "." + extension);
    }

    public static @NotNull ResourceLocation getBlock(String blockName) {
        return getLocation("blocks", blockName, "json");
    }

    public static @NotNull ResourceLocation getBlockTexture(String blockTextureName) {
        return getLocation("textures/blocks", blockTextureName, "png");
    }

    public static @NotNull ResourceLocation getBlockModel(String blockModelName) {
        return getLocation("models/blocks", blockModelName, "json");
    }

    public static @NotNull ResourceLocation getBlockEvents(String blockEventsName) {
        return getLocation("block_events", blockEventsName, "json");
    }

    public static @NotNull ResourceLocation getTexture(String textureName) {
        return getLocation("textures", textureName, "png");
    }

    public static @NotNull ResourceLocation getLanguage(String languageName) {
        return getLocation("lang", languageName, "json");
    }

    public static @NotNull ResourceLocation getFontTexture(String fontTextureName) {
        return getLocation("lang/textures", fontTextureName, "png");
    }

    public static @NotNull ResourceLocation getShader(String shaderName) {
        return getLocation("shaders", shaderName, "glsl");
    }
}