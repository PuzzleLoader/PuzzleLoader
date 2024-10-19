package com.github.puzzle.game.common.excluded;

import com.github.puzzle.core.loader.launch.PuzzleClassLoader;
import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.CommonTransformerInitializer;

public class PuzzleTransformers implements CommonTransformerInitializer {
    @Override
    public void onTransformerInit(PuzzleClassLoader classLoader) {
        classLoader.registerTransformer("com.github.puzzle.core.loader.transformers.AccessManipulatorTransformer");
    }
}
