package com.github.puzzle.game.engine.server_stages;

import com.github.puzzle.core.loader.provider.mod.ModContainer;
import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.ServerModInitializer;
import com.github.puzzle.core.loader.util.ModLocator;
//import com.github.puzzle.core.terminal.PPLTerminalConsole;
import com.github.puzzle.game.PuzzleRegistries;
import com.github.puzzle.game.engine.ServerGameLoader;
import com.github.puzzle.game.engine.ServerLoadStage;
import com.github.puzzle.game.events.OnRegisterLanguageEvent;

import static com.github.puzzle.core.Constants.MOD_ID;

public class Initialize extends ServerLoadStage {

    @Override
    public void initialize(ServerGameLoader loader) {
        super.initialize(loader);
    }

    int counter = 0;
    @Override
    public void doStage() {
        super.doStage();

        if (ModLocator.locatedMods == null) ModLocator.getMods();

        try {
            ModLocator.locatedMods.get(MOD_ID).invokeEntrypoint(ServerModInitializer.ENTRYPOINT_KEY, ServerModInitializer.class, ServerModInitializer::onInit);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ModLocator.locatedMods.keySet().forEach(containerID -> {
            ModContainer container = ModLocator.locatedMods.get(containerID);
            int counterLimiter = ModLocator.locatedMods.size() >= 100 ? 10 : 1;
            try {
                if (!container.ID.equals(MOD_ID)) {
                    if (counter >= counterLimiter) {
                        counter = 0;
                    } else counter++;
                    container.invokeEntrypoint(ServerModInitializer.ENTRYPOINT_KEY, ServerModInitializer.class, ServerModInitializer::onInit);
                } else counter++;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        PuzzleRegistries.EVENT_BUS.post(new OnRegisterLanguageEvent());
    }
}
