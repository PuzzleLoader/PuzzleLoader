package com.github.puzzle.game.engine.stages;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.github.puzzle.core.localization.TranslationKey;
import com.github.puzzle.game.PuzzleRegistries;
import com.github.puzzle.game.engine.ServerGameLoader;
import com.github.puzzle.game.engine.ServerLoadStage;
import com.github.puzzle.game.events.OnLoadAssetsEvent;
import com.github.puzzle.game.events.OnLoadAssetsFinishedEvent;
import com.github.puzzle.game.resources.PuzzleGameAssetLoader;
import com.github.puzzle.game.resources.VanillaAssetLocations;
import de.pottgames.tuningfork.SoundBuffer;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.util.Identifier;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class LoadingAssets extends ServerLoadStage {

    @Override
    public void initialize(ServerGameLoader loader) {
        super.initialize(loader);
    }

    @Subscribe
    public void onEvent(OnLoadAssetsEvent event) {
        List<Identifier> sounds = new ArrayList<>(VanillaAssetLocations.getInternalFiles("sounds/", ".ogg"));
        for (String space : GameAssetLoader.getAllNamespaces()) {
            sounds.addAll(VanillaAssetLocations.getVanillaModFiles(space, "sounds/", ".ogg"));
        }
        sounds.forEach( location -> PuzzleGameAssetLoader.LOADER.loadResource(location, SoundBuffer.class) );
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
