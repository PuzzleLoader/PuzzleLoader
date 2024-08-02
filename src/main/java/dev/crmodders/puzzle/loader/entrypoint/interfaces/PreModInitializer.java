package dev.crmodders.puzzle.loader.entrypoint.interfaces;

import dev.crmodders.puzzle.annotations.Stable;
import dev.crmodders.puzzle.util.PuzzleEntrypointUtil;

@Stable
public interface PreModInitializer {
    String ENTRYPOINT_KEY = "preInit";

    void onPreInit();

    static void invokeEntrypoint() {
        PuzzleEntrypointUtil.invoke(ENTRYPOINT_KEY, PreModInitializer.class, PreModInitializer::onPreInit);
    }
}
