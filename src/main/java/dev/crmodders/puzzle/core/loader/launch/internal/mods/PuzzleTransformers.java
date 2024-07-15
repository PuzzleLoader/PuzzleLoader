package dev.crmodders.puzzle.core.loader.launch.internal.mods;

import dev.crmodders.puzzle.core.loader.entrypoint.interfaces.TransformerInitializer;
import dev.crmodders.puzzle.core.loader.launch.PuzzleClassLoader;

public class PuzzleTransformers implements TransformerInitializer {

    @Override
    public void onTransformerInit(PuzzleClassLoader classLoader) {
        classLoader.registerTransformer("dev.crmodders.puzzle.core.loader.launch.internal.transformers.AccessManipulatorTransformer");
    }

}