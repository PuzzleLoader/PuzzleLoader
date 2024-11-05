package com.github.puzzle.game.engine.stages;

import com.github.puzzle.core.loader.launch.provider.mod.entrypoint.impls.ClientModInitializer;
import com.github.puzzle.core.loader.provider.mod.ModContainer;
import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.ModInitializer;
import com.github.puzzle.core.loader.util.ModLocator;
import com.github.puzzle.core.localization.TranslationKey;
import com.github.puzzle.game.PuzzleRegistries;
import com.github.puzzle.game.engine.ClientGameLoader;
import com.github.puzzle.game.engine.LoadStage;
import com.github.puzzle.game.events.OnRegisterLanguageEvent;

import java.util.concurrent.atomic.AtomicInteger;

import static com.github.puzzle.core.Constants.MOD_ID;

public class Initialize extends LoadStage {

    @Override
    public void initialize(ClientGameLoader loader) {
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
        try {
            try {
                ModLocator.locatedMods.get(MOD_ID).invokeEntrypoint(ModInitializer.ENTRYPOINT_KEY, ModInitializer.class, ModInitializer::onInit);
            } catch (Exception e) {}
            ModLocator.locatedMods.get(MOD_ID).invokeEntrypoint(ClientModInitializer.ENTRYPOINT_KEY, ClientModInitializer.class, ClientModInitializer::onInit);
        } catch (Exception e) {
        }

        ModLocator.locatedMods.keySet().forEach(containerID -> {
            ModContainer container = ModLocator.locatedMods.get(containerID);
            try {
                if (!container.ID.equals(MOD_ID)) {
                    container.invokeEntrypoint(ModInitializer.ENTRYPOINT_KEY, ModInitializer.class, ModInitializer::onInit);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        ModLocator.locatedMods.keySet().forEach(containerID -> {
            ModContainer container = ModLocator.locatedMods.get(containerID);
            int counterLimiter = ModLocator.locatedMods.size() >= 100 ? 10 : 1;
            try {
                if (!container.ID.equals(MOD_ID)) {
                    if (counter >= counterLimiter) {
                        String str = "Loading Mod: " + container.NAME + " | " + progress.get() + "/" + ModLocator.locatedMods.size();
                        loader.progressBarText2.setText(str);
                        loader.progressBar2.setValue(progress.get());
                        counter = 0;
                    } else counter++;
                    container.invokeEntrypoint(ClientModInitializer.ENTRYPOINT_KEY, ClientModInitializer.class, ClientModInitializer::onInit);
                } else counter++;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            progress.getAndIncrement();
        });

        PuzzleRegistries.EVENT_BUS.post(new OnRegisterLanguageEvent());
    }
}
