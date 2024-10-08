package com.github.puzzle.game.engine.stages;

import com.github.puzzle.core.loader.provider.mod.ModContainer;
import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.PostModInitializer;
import com.github.puzzle.core.loader.util.ModLocator;
import com.github.puzzle.core.localization.TranslationKey;
import com.github.puzzle.game.engine.GameLoader;
import com.github.puzzle.game.engine.LoadStage;
import com.github.puzzle.game.engine.ServerGameLoader;

import static com.github.puzzle.core.Constants.MOD_ID;

public class PostInitialize extends LoadStage {

    @Override
    public void initialize(GameLoader loader) {
        super.initialize(loader);
        title = new TranslationKey("puzzle-loader:loading_menu.initializing");
    }

    @Override
    public void doStage() {
        super.doStage();

        try {
            ModLocator.locatedMods.get(MOD_ID).invokeEntrypoint(PostModInitializer.ENTRYPOINT_KEY, PostModInitializer.class, PostModInitializer::onPostInit);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ModLocator.locatedMods.keySet().forEach(containerID -> {
            ModContainer container = ModLocator.locatedMods.get(containerID);
            try {
                if (!container.ID.equals(MOD_ID)) {
                    container.invokeEntrypoint(PostModInitializer.ENTRYPOINT_KEY, PostModInitializer.class, PostModInitializer::onPostInit);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
