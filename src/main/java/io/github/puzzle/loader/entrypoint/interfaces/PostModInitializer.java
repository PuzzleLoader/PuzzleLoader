package io.github.puzzle.loader.entrypoint.interfaces;

import io.github.puzzle.annotations.Stable;
import io.github.puzzle.util.PuzzleEntrypointUtil;

@Stable
public interface PostModInitializer {
    String ENTRYPOINT_KEY = "postInit";

    void onPostInit();

    static void invokeEntrypoint() {
        PuzzleEntrypointUtil.invoke(ENTRYPOINT_KEY, PostModInitializer.class, PostModInitializer::onPostInit);
    }

}
