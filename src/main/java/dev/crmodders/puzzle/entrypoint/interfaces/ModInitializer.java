package dev.crmodders.puzzle.entrypoint.interfaces;

import dev.crmodders.puzzle.annotations.Stable;
import dev.crmodders.puzzle.utils.PuzzleEntrypointUtil;

/* The Main init entrypoint for PuzzleLoader */

@Stable
public interface ModInitializer {

    String ENTRYPOINT_KEY = "init";

    void onInit();

    static void invokeEntrypoint() {
        PuzzleEntrypointUtil.invoke(ENTRYPOINT_KEY, ModInitializer.class, ModInitializer::onInit);
    }

}
