package dev.crmodders.puzzle.entrypoint.interfaces;

import dev.crmodders.puzzle.annotations.Stable;
import dev.crmodders.puzzle.utils.PuzzleEntrypointUtil;

/* The Main init entrypoint for PuzzleLoader */

@Stable
public interface PreMixinModInitializer {

    String ENTRYPOINT_KEY = "preMixinInject";

    void onPreMixinInject();

    static void invokeEntrypoint() {
        PuzzleEntrypointUtil.invoke(ENTRYPOINT_KEY, PreMixinModInitializer.class, PreMixinModInitializer::onPreMixinInject);
    }

}
