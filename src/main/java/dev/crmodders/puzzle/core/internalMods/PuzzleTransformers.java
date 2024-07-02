package dev.crmodders.puzzle.core.internalMods;

import dev.crmodders.puzzle.core.entrypoint.interfaces.TransformerInitializer;
import dev.crmodders.puzzle.core.launch.PuzzleClassLoader;

public class PuzzleTransformers implements TransformerInitializer {

    @Override
    public void onTransformerInit(PuzzleClassLoader classLoader) {
        classLoader.registerTransformer("dev.crmodders.puzzle.core.launch.transformers.ClassPublisizor");
    }

}
