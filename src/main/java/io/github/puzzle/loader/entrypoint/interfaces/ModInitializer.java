package io.github.puzzle.loader.entrypoint.interfaces;

import io.github.puzzle.annotations.Stable;
import io.github.puzzle.util.PuzzleEntrypointUtil;

@Stable
public interface ModInitializer {
    String ENTRYPOINT_KEY = "init";

    void onInit();

    static void invokeEntrypoint() {
        PuzzleEntrypointUtil.invoke(ENTRYPOINT_KEY, ModInitializer.class, ModInitializer::onInit);
    }
}
