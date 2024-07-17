package dev.crmodders.puzzle.loader.launch.internal.mods;

import dev.crmodders.puzzle.loader.entrypoint.interfaces.TransformerInitializer;
import dev.crmodders.puzzle.loader.launch.PuzzleClassLoader;
import org.jetbrains.annotations.NotNull;

public class PuzzleTransformers implements TransformerInitializer {
    @Override
    public void onTransformerInit(@NotNull PuzzleClassLoader classLoader) {
        classLoader.registerTransformer("dev.crmodders.puzzle.loader.launch.internal.transformers.AccessManipulatorTransformer");
    }
}
