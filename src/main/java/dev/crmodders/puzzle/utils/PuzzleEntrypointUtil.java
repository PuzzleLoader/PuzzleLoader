package dev.crmodders.puzzle.utils;

import dev.crmodders.puzzle.core.loader.mod.ModLocator;

import java.util.function.Consumer;

public interface PuzzleEntrypointUtil {

    static <T> void invoke(String key, Class<T> entrypointType, Consumer<? super T> entrypointInvoker) {
        if (ModLocator.LocatedMods == null) ModLocator.getMods();
        ModLocator.LocatedMods.values().forEach(modContainer -> {
            modContainer.invokeEntrypoint(key, entrypointType, entrypointInvoker);
        });
    }

}
