package dev.crmodders.puzzle.game.engine.stages;

import dev.crmodders.puzzle.core.Identifier;
import dev.crmodders.puzzle.core.PuzzleRegistries;
import dev.crmodders.puzzle.core.localization.TranslationKey;
import dev.crmodders.puzzle.game.engine.GameLoader;
import dev.crmodders.puzzle.game.engine.LoadStage;

import java.util.Set;

public class PostInitialize extends LoadStage {

    @Override
    public void initialize(GameLoader loader) {
        super.initialize(loader);
        title = new TranslationKey("puzzle-loader:loading_menu.initializing");
    }

    @Override
    public void doStage() {
        super.doStage();

        Set<Identifier> modIds = PuzzleRegistries.ON_POST_INIT.names();
        loader.setupProgressBar(loader.progressBar2, modIds.size(), "Initializing Mods: PostInit");
        for(Identifier modId : modIds) {
            loader.incrementProgress(loader.progressBar2, modId.name);
            Runnable runnable = PuzzleRegistries.ON_POST_INIT.get(modId);
            runnable.run();
        }

    }
}
