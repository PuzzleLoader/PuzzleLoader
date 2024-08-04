package com.github.puzzle.game.engine.stages;

import com.github.puzzle.core.localization.TranslationKey;
import com.github.puzzle.game.engine.GameLoader;
import com.github.puzzle.game.engine.LoadStage;
import com.github.puzzle.loader.entrypoint.interfaces.PostModInitializer;

public class PostInitialize extends LoadStage {

    @Override
    public void initialize(GameLoader loader) {
        super.initialize(loader);
        title = new TranslationKey("puzzle-loader:loading_menu.initializing");
    }

    @Override
    public void doStage() {
        super.doStage();
        PostModInitializer.invokeEntrypoint();
    }
}
