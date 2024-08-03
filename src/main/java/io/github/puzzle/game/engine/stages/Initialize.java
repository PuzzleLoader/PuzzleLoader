package io.github.puzzle.game.engine.stages;

import io.github.puzzle.core.localization.TranslationKey;
import io.github.puzzle.game.engine.GameLoader;
import io.github.puzzle.game.engine.LoadStage;
import io.github.puzzle.loader.entrypoint.interfaces.ModInitializer;
import io.github.puzzle.loader.mod.ModContainer;
import io.github.puzzle.loader.mod.ModLocator;

public class Initialize extends LoadStage {

    @Override
    public void initialize(GameLoader loader) {
        super.initialize(loader);
        title = new TranslationKey("puzzle-loader:loading_menu.initializing");
    }

    int counter = 0;
    @Override
    public void doStage() {
        super.doStage();

        if (ModLocator.locatedMods == null) ModLocator.getMods();

        int progress = 0;
        loader.setupProgressBar(loader.progressBar2, ModLocator.locatedMods.size(), "Initializing Mods: Init");
        ModLocator.locatedMods.keySet().forEach(containerID -> {
            ModContainer container = ModLocator.locatedMods.get(containerID);
            int counterLimiter = ModLocator.locatedMods.size() >= 100 ? 10 : 1;
            try {
                if (counter >= counterLimiter) {
                    String str = "Loading Mod: " + container.NAME + " | %d/%d";
                    loader.progressBarText2.setText(str);
                    loader.progressBar2.setValue(progress);
                    counter = 0;
                } else counter++;
                container.invokeEntrypoint(ModInitializer.ENTRYPOINT_KEY, ModInitializer.class, ModInitializer::onInit);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }
}
