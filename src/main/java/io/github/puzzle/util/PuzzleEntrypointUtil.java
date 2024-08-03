package io.github.puzzle.util;

import io.github.puzzle.loader.mod.ModContainer;
import io.github.puzzle.loader.mod.ModLocator;

import java.util.function.Consumer;

public class PuzzleEntrypointUtil {
    public static <T> void invoke(String key, Class<T> entrypointType, Consumer<? super T> entrypointInvoker) {
        if (ModLocator.locatedMods == null) ModLocator.getMods();
        ModLocator.locatedMods.keySet().forEach(containerID -> {
            ModContainer container = ModLocator.locatedMods.get(containerID);
            try {
                container.invokeEntrypoint(key, entrypointType, entrypointInvoker);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
