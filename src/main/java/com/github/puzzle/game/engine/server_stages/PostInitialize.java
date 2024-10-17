package com.github.puzzle.game.engine.server_stages;

import com.github.puzzle.core.loader.provider.mod.ModContainer;
import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.PostModInitializer;
import com.github.puzzle.core.loader.util.ModLocator;
import com.github.puzzle.core.localization.TranslationKey;
import com.github.puzzle.game.engine.ServerGameLoader;
import com.github.puzzle.game.engine.ServerLoadStage;

import static com.github.puzzle.core.Constants.MOD_ID;

public class PostInitialize extends ServerLoadStage {

    @Override
    public void initialize(ServerGameLoader loader) {
        super.initialize(loader);
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
