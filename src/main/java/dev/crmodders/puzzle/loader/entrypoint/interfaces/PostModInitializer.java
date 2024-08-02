package dev.crmodders.puzzle.loader.entrypoint.interfaces;

import dev.crmodders.puzzle.annotations.Stable;
import dev.crmodders.puzzle.util.PuzzleEntrypointUtil;

@Stable
public interface PostModInitializer {
    String ENTRYPOINT_KEY = "postInit";

    void onPostInit();

    static void invokeEntrypoint() {
        PuzzleEntrypointUtil.invoke(ENTRYPOINT_KEY, PostModInitializer.class, PostModInitializer::onPostInit);
    }

}
