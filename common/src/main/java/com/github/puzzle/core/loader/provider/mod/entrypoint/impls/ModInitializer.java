package com.github.puzzle.core.loader.provider.mod.entrypoint.impls;

import com.github.puzzle.core.loader.util.PuzzleEntrypointUtil;

public interface ModInitializer {
    String ENTRYPOINT_KEY = "init";

    void onInit();

    static void invokeEntrypoint() {
        PuzzleEntrypointUtil.invoke(ENTRYPOINT_KEY, ModInitializer.class, ModInitializer::onInit);
    }
}
