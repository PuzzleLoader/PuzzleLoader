package com.github.puzzle.core.loader.provider.mod.entrypoint.impls;

import com.github.puzzle.core.loader.util.PuzzleEntrypointUtil;

public interface ServerPreModInitializer {
    String ENTRYPOINT_KEY = "server_preInit";

    void onPreInit();

    static void invokeEntrypoint() {
        PuzzleEntrypointUtil.invoke(ENTRYPOINT_KEY, ServerPreModInitializer.class, ServerPreModInitializer::onPreInit);
    }
}
