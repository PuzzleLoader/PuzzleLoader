package com.github.puzzle.core.loader.provider.mod.entrypoint.impls;

import com.github.puzzle.core.loader.util.PuzzleEntrypointUtil;

public interface PostModInitializer {
    String ENTRYPOINT_KEY = "postInit";

    void onPostInit();

    static void invokeEntrypoint() {
        PuzzleEntrypointUtil.invoke(ENTRYPOINT_KEY, PostModInitializer.class, PostModInitializer::onPostInit);
    }
}
