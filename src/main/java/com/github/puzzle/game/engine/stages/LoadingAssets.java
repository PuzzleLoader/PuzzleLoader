package com.github.puzzle.game.engine.stages;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.github.puzzle.core.PuzzleRegistries;
import com.github.puzzle.core.localization.TranslationKey;
import com.github.puzzle.core.resources.PuzzleGameAssetLoader;
import com.github.puzzle.core.resources.ResourceLocation;
import com.github.puzzle.core.resources.VanillaAssetLocations;
import com.github.puzzle.game.engine.GameLoader;
import com.github.puzzle.game.engine.LoadStage;
import com.github.puzzle.game.events.OnLoadAssetsEvent;
import com.github.puzzle.game.events.OnLoadAssetsFinishedEvent;
import com.github.puzzle.game.items.IModItem;
import de.pottgames.tuningfork.SoundBuffer;
import finalforeach.cosmicreach.items.Item;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class LoadingAssets extends LoadStage {

    private static final TranslationKey TEXT_TITLE = new TranslationKey("puzzle-loader:loading_menu.registering_assets");
    private static final TranslationKey TEXT_LOADING_ASSETS = new TranslationKey("puzzle-loader:loading_menu.loading_assets");

    @Override
    public void initialize(GameLoader loader) {
        super.initialize(loader);
        title = TEXT_TITLE;
    }

    @Subscribe
    public void onEvent(OnLoadAssetsEvent event) {
        List<ResourceLocation> textures = new ArrayList<>();
        textures.addAll(VanillaAssetLocations.getInternalFiles("textures/ui", ".png"));
        textures.addAll(VanillaAssetLocations.getInternalFiles("textures/items", ".png"));
        textures.addAll(VanillaAssetLocations.getInternalFiles("textures/entities", ".png"));
        textures.addAll(VanillaAssetLocations.getInternalFiles("lang/textures/", ".png"));

        for (Item item : Item.allItems.values()) {
            if (item instanceof IModItem modItem) textures.add(modItem.getTexturePath());
        }

        textures.forEach( location -> PuzzleGameAssetLoader.LOADER.loadResource(location, Texture.class) );

        List<ResourceLocation> sounds = new ArrayList<>();
        sounds.addAll(VanillaAssetLocations.getInternalFiles("sounds/", ".ogg"));
        sounds.addAll(VanillaAssetLocations.getVanillaModFiles("sounds/", ".ogg"));
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
        tasks.add( () -> loader.setupProgressBar(loader.progressBar2, manager.getQueuedAssets(), TEXT_LOADING_ASSETS) );
        for(int i = 0; i < manager.getQueuedAssets(); i++) {
            tasks.add( () -> loader.incrementProgress(loader.progressBar2) );
            tasks.add(manager::update);
        }
        // let's be safe
        tasks.add(manager::finishLoading);
        tasks.add( () -> PuzzleRegistries.EVENT_BUS.post(new OnLoadAssetsFinishedEvent()) );
        return tasks;
    }

}
