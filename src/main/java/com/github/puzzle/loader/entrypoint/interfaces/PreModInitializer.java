package com.github.puzzle.loader.entrypoint.interfaces;

import com.github.puzzle.annotations.Stable;
import com.github.puzzle.util.PuzzleEntrypointUtil;

@Stable
public interface PreModInitializer {
    String ENTRYPOINT_KEY = "preInit";

    void onPreInit();

    static void invokeEntrypoint() {
        PuzzleEntrypointUtil.invoke(ENTRYPOINT_KEY, PreModInitializer.class, PreModInitializer::onPreInit);
    }
}
