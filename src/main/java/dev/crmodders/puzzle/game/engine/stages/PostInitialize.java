package dev.crmodders.puzzle.game.engine.stages;

import dev.crmodders.puzzle.core.localization.TranslationKey;
import dev.crmodders.puzzle.game.engine.GameLoader;
import dev.crmodders.puzzle.game.engine.LoadStage;
import dev.crmodders.puzzle.loader.entrypoint.interfaces.PostModInitializer;

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
