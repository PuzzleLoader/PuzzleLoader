package com.github.puzzle.core.loader.provider.mod.entrypoint.impls;

import com.github.puzzle.core.loader.util.PuzzleEntrypointUtil;

public interface ServerModInitializer {
    String ENTRYPOINT_KEY = "server_init";

    void onInit();

    static void invokeEntrypoint() {
        PuzzleEntrypointUtil.invoke(ENTRYPOINT_KEY, ServerModInitializer.class, ServerModInitializer::onInit);
    }
}
