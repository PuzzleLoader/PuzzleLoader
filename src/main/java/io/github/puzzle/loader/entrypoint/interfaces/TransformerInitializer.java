package io.github.puzzle.loader.entrypoint.interfaces;

import io.github.puzzle.loader.launch.PuzzleClassLoader;
import io.github.puzzle.util.PuzzleEntrypointUtil;

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
