package dev.crmodders.puzzle.core.entrypoint.interfaces;

import dev.crmodders.puzzle.annotations.Stable;
import dev.crmodders.puzzle.core.launch.Piece;
import dev.crmodders.puzzle.utils.PuzzleEntrypointUtil;

/* The Main init entrypoint for PuzzleLoader */
@Stable
public interface ModInitializer {

    String ENTRYPOINT_KEY = "init";

    void onInit();

    static void invokeEntrypoint() {
        Piece.MOD_LAUNCH_STATE = Piece.LAUNCH_STATE.INIT;

        PuzzleEntrypointUtil.invoke(ENTRYPOINT_KEY, ModInitializer.class, ModInitializer::onInit);
        Piece.MOD_LAUNCH_STATE = Piece.LAUNCH_STATE.IN_GAME;
    }

}
