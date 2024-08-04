package com.github.puzzle.loader.entrypoint.interfaces;

import com.github.puzzle.annotations.Stable;
import com.github.puzzle.util.PuzzleEntrypointUtil;

@Stable
public interface ModInitializer {
    String ENTRYPOINT_KEY = "init";

    void onInit();

    static void invokeEntrypoint() {
        PuzzleEntrypointUtil.invoke(ENTRYPOINT_KEY, ModInitializer.class, ModInitializer::onInit);
    }
}
