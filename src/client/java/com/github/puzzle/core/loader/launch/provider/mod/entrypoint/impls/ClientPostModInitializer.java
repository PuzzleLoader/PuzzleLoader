package com.github.puzzle.core.loader.launch.provider.mod.entrypoint.impls;

import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.PostModInitializer;
import com.github.puzzle.core.loader.util.PuzzleEntrypointUtil;

public interface ClientPostModInitializer {
    String ENTRYPOINT_KEY = "client_postInit";

    void onPostInit();

    static void invokeEntrypoint() {
        PostModInitializer.invokeEntrypoint();
        PuzzleEntrypointUtil.invoke(ENTRYPOINT_KEY, ClientPostModInitializer.class, ClientPostModInitializer::onPostInit);
    }

}
