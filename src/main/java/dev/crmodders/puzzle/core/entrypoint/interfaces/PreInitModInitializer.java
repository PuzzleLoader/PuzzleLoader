package dev.crmodders.puzzle.core.entrypoint.interfaces;

import dev.crmodders.puzzle.annotations.Stable;
import dev.crmodders.puzzle.core.launch.Piece;
import dev.crmodders.puzzle.utils.PuzzleEntrypointUtil;

/* The pre initializer entrypoint for PuzzleLoader */
@Stable
public interface PreInitModInitializer {

    String ENTRYPOINT_KEY = "preInit";

    void onPreInit();

    static void invokeEntrypoint() {
        Piece.MOD_LAUNCH_STATE = Piece.LAUNCH_STATE.PRE_INIT;
        PuzzleEntrypointUtil.invoke(ENTRYPOINT_KEY, PreInitModInitializer.class, PreInitModInitializer::onPreInit);
    }


}
