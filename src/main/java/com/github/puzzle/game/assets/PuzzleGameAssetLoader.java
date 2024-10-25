package com.github.puzzle.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.github.puzzle.core.loader.util.AnsiColours;
import com.github.puzzle.core.localization.files.LanguageFileVersion1;
import de.pottgames.tuningfork.SoundBuffer;
import finalforeach.cosmicreach.io.SaveLocation;
import finalforeach.cosmicreach.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PuzzleGameAssetLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger("Puzzle | AssetLoader");

    public static FileHandle locateAsset(String fileName) {
        return locateAsset(Identifier.of(fileName));
    }
    public static boolean assetExists(String fileName) {
        return assetExists(Identifier.of(fileName));
    }


    public static boolean assetExists(@NotNull Identifier location) {
        // Split Locations
        FileHandle splitModLocationFile = Gdx.files.absolute(SaveLocation.getSaveFolderLocation() + "/mods/%s/%s".formatted(location.getNamespace(), location.getName()));
        FileHandle splitVanillaLocationFile = Gdx.files.internal("%s/%s".formatted(location.getNamespace(), location.getName()));
        FileHandle splitJavaModLocationFile = Gdx.files.classpath("assets/%s/%s".formatted(location.getNamespace(), location.getName()));

        return splitVanillaLocationFile.exists() || splitModLocationFile.exists() || splitJavaModLocationFile.exists();
    }

    public static @Nullable FileHandle locateAsset(@NotNull Identifier location) {
        // Fix asset loading bug
        FileHandle modLocationFile = Gdx.files.absolute(SaveLocation.getSaveFolderLocation() + "/mods/%s/%s".formatted(location.getNamespace(), location.getName()));
        if (modLocationFile.exists()) {
            LOGGER.info("Loading " + AnsiColours.CYAN+"\"{}\"" + AnsiColours.WHITE + " from Mods Folder", location.getName());
            return modLocationFile;
        }

        FileHandle classpathLocationFile = Gdx.files.classpath("assets/%s/%s".formatted(location.getNamespace(), location.getName()));
        if (classpathLocationFile.exists()) {
            LOGGER.info("Loading " + AnsiColours.PURPLE + "\"{}\"" + AnsiColours.WHITE + " from Java Mod " + AnsiColours.GREEN + "\"{}\"" + AnsiColours.WHITE, location.getName(), location.getNamespace());
            return classpathLocationFile;
        }

        FileHandle vanillaLocationFile = Gdx.files.internal("%s/%s".formatted(location.getNamespace(), location.getName()));
        if (vanillaLocationFile.exists()) {
            LOGGER.info("Loading " + AnsiColours.YELLOW + "\"{}\""+AnsiColours.WHITE+" from Cosmic Reach", location.getName());
            return vanillaLocationFile;
        }

        LOGGER.error("Cannot find the resource {} | {}", location, location.toPath());
        return null;
    }

    public static final PuzzleGameAssetLoader LOADER = new PuzzleGameAssetLoader();

    private final AssetManager assetManager;

    public PuzzleGameAssetLoader() {
        FileHandleResolver resolver = PuzzleGameAssetLoader::locateAsset;
        assetManager = new AssetManager(resolver);
        assetManager.setLoader(SoundBuffer.class, new TuningForkLoader(resolver));
        assetManager.setLoader(LanguageFileVersion1.class, new LanguageFileLoader(resolver));
    }

    public <T> void load(String fileName, Class<T> assetClass) {
        assetManager.load(fileName, assetClass);
    }

    public <T> T loadSync(String fileName, Class<T> assetClass) {
        assetManager.load(fileName, assetClass);
        return assetManager.finishLoadingAsset(fileName);
    }

    public <T> T get(String fileName, Class<T> assetClass) {
        if(!assetManager.isLoaded(fileName)) {
            LOGGER.error("Asset not loaded {} ({}) loading now", fileName, assetClass.getSimpleName());
            return loadSync(fileName, assetClass);
        }
        return assetManager.get(fileName);
    }

    public void unload(String fileName) {
        assetManager.unload(fileName);
    }

    public <T> void loadResource(@NotNull Identifier location, Class<T> assetClass) {
        assetManager.load(location.toString(), assetClass);
    }

    public <T> T loadResourceSync(@NotNull Identifier location, Class<T> assetClass) {
        assetManager.load(location.toString(), assetClass);
        return assetManager.finishLoadingAsset(location.toString());
    }

    public <T> T getResource(@NotNull Identifier location, Class<T> assetClass) {
        if(!assetManager.isLoaded(location.toString())) {
            LOGGER.warn("Asset not loaded {} ({}) loading now", location, assetClass.getSimpleName());
            return loadResourceSync(location, assetClass);
        }
        return assetManager.get(location.toString(), assetClass);
    }

    public void unloadResource(@NotNull Identifier location) {
        assetManager.unload(location.toString());
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}