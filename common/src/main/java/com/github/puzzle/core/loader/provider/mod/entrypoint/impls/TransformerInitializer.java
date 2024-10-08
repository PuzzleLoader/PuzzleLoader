package com.github.puzzle.core.loader.provider.mod.entrypoint.impls;

import com.github.puzzle.core.loader.launch.PuzzleClassLoader;
import com.github.puzzle.core.loader.util.PuzzleEntrypointUtil;

public interface TransformerInitializer {
    String ENTRYPOINT_KEY = "transformers";

    void onTransformerInit(PuzzleClassLoader classLoader);

    static void invokeTransformers(PuzzleClassLoader classLoader) {
        PuzzleEntrypointUtil.invoke(ENTRYPOINT_KEY,
                TransformerInitializer.class,
                transformerInitializer -> {
                    transformerInitializer.onTransformerInit(classLoader);
                });
    }
}
