package com.github.puzzle.loader.launch.internal.mods;

import com.github.puzzle.loader.entrypoint.interfaces.TransformerInitializer;
import com.github.puzzle.loader.launch.PuzzleClassLoader;
import org.jetbrains.annotations.NotNull;

public class PuzzleTransformers implements TransformerInitializer {
    @Override
    public void onTransformerInit(@NotNull PuzzleClassLoader classLoader) {
        classLoader.registerTransformer("com.github.puzzle.loader.launch.internal.transformers.AccessManipulatorTransformer");
    }
}
