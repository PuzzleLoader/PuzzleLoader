package com.github.puzzle.core.loader.launch.provider.mod.entrypoint.impls;

import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.PreModInitializer;
import com.github.puzzle.core.loader.util.PuzzleEntrypointUtil;

public interface ClientPreModInitializer {
    String ENTRYPOINT_KEY = "client_preInit";

    void onPreInit();

    static void invokeEntrypoint() {
        PreModInitializer.invokeEntrypoint();
        PuzzleEntrypointUtil.invoke(ENTRYPOINT_KEY, ClientPreModInitializer.class, ClientPreModInitializer::onPreInit);
    }
}
