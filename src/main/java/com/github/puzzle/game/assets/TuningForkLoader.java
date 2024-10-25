package com.github.puzzle.game.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import de.pottgames.tuningfork.SoundBuffer;
import de.pottgames.tuningfork.SoundLoader;

public class TuningForkLoader extends AsynchronousAssetLoader<SoundBuffer, TuningForkLoader.TuningForkParameters> {
    public TuningForkLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, TuningForkParameters p) {

    }

    @Override
    public SoundBuffer loadSync(AssetManager manager, String fileName, FileHandle file, TuningForkParameters p) {
        if(p == null) p = new TuningForkParameters();
        return SoundLoader.load(file, p.reverse);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String s, FileHandle fileHandle, TuningForkParameters p) {
        return null;
    }

    public static class TuningForkParameters extends AssetLoaderParameters<SoundBuffer> {
        public boolean reverse = false;
    }
}