package io.github.puzzle.loader.entrypoint.interfaces;

import io.github.puzzle.annotations.Stable;
import io.github.puzzle.util.PuzzleEntrypointUtil;

@Stable
public interface PreModInitializer {
    String ENTRYPOINT_KEY = "preInit";

    void onPreInit();

    static void invokeEntrypoint() {
        PuzzleEntrypointUtil.invoke(ENTRYPOINT_KEY, PreModInitializer.class, PreModInitializer::onPreInit);
    }
}
