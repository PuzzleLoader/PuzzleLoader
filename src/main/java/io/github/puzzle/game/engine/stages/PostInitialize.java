package io.github.puzzle.game.engine.stages;

import io.github.puzzle.core.localization.TranslationKey;
import io.github.puzzle.game.engine.GameLoader;
import io.github.puzzle.game.engine.LoadStage;
import io.github.puzzle.loader.entrypoint.interfaces.PostModInitializer;

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
