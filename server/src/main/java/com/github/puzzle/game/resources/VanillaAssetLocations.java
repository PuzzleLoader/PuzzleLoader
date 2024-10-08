package com.github.puzzle.game.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import finalforeach.cosmicreach.io.SaveLocation;
import finalforeach.cosmicreach.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VanillaAssetLocations {
    private static String[] internalFilesList = null;

    public static @NotNull List<Identifier> getInternalFiles(String folder, String extension) {
        List<Identifier> files = new ArrayList<>();
        if(internalFilesList == null) internalFilesList = Gdx.files.internal("assets.txt").readString().split("\n");
        for(String internalFileName : internalFilesList) {
            if (internalFileName.startsWith("base/" + folder) && internalFileName.endsWith(extension) && Gdx.files.internal(internalFileName).exists()) {
                files.add(Identifier.of(internalFileName.replace("base/", "base:")));
            }
        }
        return files;
    }

    public static FileHandle getModsFolder() {
        return Gdx.files.absolute(SaveLocation.getSaveFolderLocation() + "/mods");
    }

    public static @NotNull List<Identifier> getVanillaModFiles(String namespace, String folder, String extension) {
        List<Identifier> files = new ArrayList<>();
        for(FileHandle f : Gdx.files.absolute(SaveLocation.getSaveFolderLocation() + "/mods/" + namespace + "/" + folder).list()) {
            String fileName = f.name();
            if (fileName.endsWith(extension)) {
                files.add(Identifier.of(namespace, fileName));
            }
        }
        return files;
    }

    private static @NotNull Identifier getLocation(String folder, String name, String extension) {
        Identifier identifier = Identifier.of(name);
        return Identifier.of(identifier.getNamespace(), identifier.getName());
    }

    public static @NotNull Identifier getBlock(String blockName) {
        return getLocation("blocks", blockName, "json");
    }

    public static @NotNull Identifier getBlockTexture(String blockTextureName) {
        return getLocation("textures/blocks", blockTextureName, "png");
    }

    public static @NotNull Identifier getBlockModel(String blockModelName) {
        Identifier id = getLocation("models/blocks", blockModelName, "json");
        id = Identifier.of(id.getNamespace(), id.getName().startsWith("models/blocks/") ? id.getName() : "models/blocks/" + id.getName());
        id = Identifier.of(id.getNamespace(), id.getName().endsWith(".json") ? id.getName() : id.getName() + ".json");
        return id;
    }

    public static @NotNull Identifier getBlockEvents(String blockEventsName) {
        return getLocation("block_events", blockEventsName, "json");
    }

    public static @NotNull Identifier getTexture(String textureName) {
        return getLocation("textures", textureName, "png");
    }

    public static @NotNull Identifier getLanguage(String languageName) {
        return getLocation("lang", languageName, "json");
    }

    public static @NotNull Identifier getFontTexture(String fontTextureName) {
        return getLocation("lang/textures", fontTextureName, "png");
    }

    public static @NotNull Identifier getShader(String shaderName) {
        return getLocation("shaders", shaderName, "glsl");
    }
}