package dev.crmodders.puzzle.utils;

import dev.crmodders.puzzle.core.loader.mod.ModContainer;
import dev.crmodders.puzzle.core.loader.mod.ModLocator;

import java.util.function.Consumer;

public interface PuzzleEntrypointUtil {

    static <T> void invoke(String key, Class<T> entrypointType, Consumer<? super T> entrypointInvoker) {
        if (ModLocator.LocatedMods == null) ModLocator.getMods();
        ModLocator.LocatedMods.keySet().forEach(containerID -> {
            ModContainer container = ModLocator.LocatedMods.get(containerID);
            container.invokeEntrypoint(key, entrypointType, entrypointInvoker);
        });
    }

}
