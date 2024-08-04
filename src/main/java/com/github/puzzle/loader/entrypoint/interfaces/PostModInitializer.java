package com.github.puzzle.loader.entrypoint.interfaces;

import com.github.puzzle.annotations.Stable;
import com.github.puzzle.util.PuzzleEntrypointUtil;

@Stable
public interface PostModInitializer {
    String ENTRYPOINT_KEY = "postInit";

    void onPostInit();

    static void invokeEntrypoint() {
        PuzzleEntrypointUtil.invoke(ENTRYPOINT_KEY, PostModInitializer.class, PostModInitializer::onPostInit);
    }

}
