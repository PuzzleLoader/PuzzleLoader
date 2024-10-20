package com.github.puzzle.game.engine.server_stages;

import com.badlogic.gdx.assets.AssetManager;
import com.github.puzzle.game.PuzzleRegistries;
import com.github.puzzle.game.engine.ServerGameLoader;
import com.github.puzzle.game.engine.ServerLoadStage;
import com.github.puzzle.game.events.OnLoadAssetsEvent;
import com.github.puzzle.game.events.OnLoadAssetsFinishedEvent;
import com.github.puzzle.game.resources.PuzzleGameAssetLoader;

import java.util.List;

public class LoadingAssets extends ServerLoadStage {

    @Override
    public void initialize(ServerGameLoader loader) {
        this.loader = loader;
    }

    @Override
    public void doStage() {
        super.doStage();
        PuzzleRegistries.EVENT_BUS.post(new OnLoadAssetsEvent());
    }

    @Override
    public List<Runnable> getGlTasks() {
        List<Runnable> tasks = super.getGlTasks();
        AssetManager manager = PuzzleGameAssetLoader.LOADER.getAssetManager();
        for(int i = 0; i < manager.getQueuedAssets(); i++) {
            tasks.add(manager::update);
        }
        // let's be safe
        tasks.add(manager::finishLoading);
        tasks.add( () -> PuzzleRegistries.EVENT_BUS.post(new OnLoadAssetsFinishedEvent()) );
        return tasks;
    }

}
