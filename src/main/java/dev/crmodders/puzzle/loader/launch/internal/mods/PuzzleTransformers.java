package dev.crmodders.puzzle.loader.launch.internal.mods;

import dev.crmodders.puzzle.loader.entrypoint.interfaces.TransformerInitializer;
import dev.crmodders.puzzle.loader.launch.PuzzleClassLoader;

public class PuzzleTransformers implements TransformerInitializer {

    @Override
    public void onTransformerInit(PuzzleClassLoader classLoader) {
        classLoader.registerTransformer("dev.crmodders.puzzle.loader.launch.internal.transformers.AccessManipulatorTransformer");
    }
}
