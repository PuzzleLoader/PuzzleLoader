package dev.crmodders.puzzle.entrypoint.interfaces;

import dev.crmodders.puzzle.annotations.Stable;
import dev.crmodders.puzzle.utils.PuzzleEntrypointUtil;

/* The pre initializer entrypoint for PuzzleLoader */
@Stable
public interface ModPreInitializer {

    String ENTRYPOINT_KEY = "preInit";

    void onPreInit();

    static void invokeEntrypoint() {
        PuzzleEntrypointUtil.invoke(ENTRYPOINT_KEY, ModInitializer.class, ModInitializer::onInit);
    }


}
