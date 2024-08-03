package io.github.puzzle.core.resources;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import io.github.puzzle.core.localization.files.LanguageFileVersion1;

import java.io.IOException;

public class LanguageFileLoader extends AsynchronousAssetLoader<LanguageFileVersion1, LanguageFileLoader.LanguageFileParameters> {
    public LanguageFileLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, LanguageFileLoader.LanguageFileParameters p) {

    }

    @Override
    public LanguageFileVersion1 loadSync(AssetManager manager, String fileName, FileHandle file, LanguageFileLoader.LanguageFileParameters p) {
        try {
            return LanguageFileVersion1.loadLanguageFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String s, FileHandle fileHandle, LanguageFileLoader.LanguageFileParameters p) {
        return null;
    }

    public static class LanguageFileParameters extends AssetLoaderParameters<LanguageFileVersion1> {
    }
}