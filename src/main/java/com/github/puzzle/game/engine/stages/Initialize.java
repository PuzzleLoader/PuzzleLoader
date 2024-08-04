package com.github.puzzle.game.engine.stages;

import com.github.puzzle.core.localization.TranslationKey;
import com.github.puzzle.game.engine.GameLoader;
import com.github.puzzle.game.engine.LoadStage;
import com.github.puzzle.loader.entrypoint.interfaces.ModInitializer;
import com.github.puzzle.loader.mod.ModContainer;
import com.github.puzzle.loader.mod.ModLocator;

import java.util.concurrent.atomic.AtomicInteger;

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

        AtomicInteger progress = new AtomicInteger();
        loader.setupProgressBar(loader.progressBar2, ModLocator.locatedMods.size(), "Initializing Mods: Init");
        ModLocator.locatedMods.keySet().forEach(containerID -> {
            ModContainer container = ModLocator.locatedMods.get(containerID);
            int counterLimiter = ModLocator.locatedMods.size() >= 100 ? 10 : 1;
            try {
                if (counter >= counterLimiter) {
                    String str = "Loading Mod: " + container.NAME + " | " + progress.get() + "/" + ModLocator.locatedMods.size();
                    loader.progressBarText2.setText(str);
                    loader.progressBar2.setValue(progress.get());
                    counter = 0;
                } else counter++;
                container.invokeEntrypoint(ModInitializer.ENTRYPOINT_KEY, ModInitializer.class, ModInitializer::onInit);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            progress.getAndIncrement();
        });

    }
}
