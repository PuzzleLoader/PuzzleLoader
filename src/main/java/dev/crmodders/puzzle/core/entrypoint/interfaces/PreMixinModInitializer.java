package dev.crmodders.puzzle.core.entrypoint.interfaces;

import dev.crmodders.puzzle.annotations.Stable;
import dev.crmodders.puzzle.core.launch.Piece;
import dev.crmodders.puzzle.utils.PuzzleEntrypointUtil;

/* The Main init entrypoint for PuzzleLoader */

@Stable
public interface PreMixinModInitializer {

    String ENTRYPOINT_KEY = "preMixinInject";

    void onPreMixinInject();

    static void invokeEntrypoint() {
        Piece.MOD_LAUNCH_STATE = Piece.LAUNCH_STATE.PRE_MIXIN_INJECT;

        PuzzleEntrypointUtil.invoke(ENTRYPOINT_KEY, PreMixinModInitializer.class, PreMixinModInitializer::onPreMixinInject);
    }

}
