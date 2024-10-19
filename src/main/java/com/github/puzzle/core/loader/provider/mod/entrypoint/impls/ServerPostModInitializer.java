package com.github.puzzle.core.loader.provider.mod.entrypoint.impls;

import com.github.puzzle.core.loader.util.PuzzleEntrypointUtil;

public interface ServerPostModInitializer {
    String ENTRYPOINT_KEY = "server_postInit";

    void onPostInit();

    static void invokeEntrypoint() {
        PuzzleEntrypointUtil.invoke(ENTRYPOINT_KEY, ServerPostModInitializer.class, ServerPostModInitializer::onPostInit);
    }
}
