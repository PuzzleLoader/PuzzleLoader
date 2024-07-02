package dev.crmodders.puzzle.utils;

import dev.crmodders.puzzle.core.mod.ModLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public interface PuzzleEntrypointUtil {

    static <T> void invoke(String key, Class<T> entrypointType, Consumer<? super T> entrypointInvoker) {
        if (ModLocator.LocatedMods == null) ModLocator.getMods();
        ModLocator.LocatedMods.values().forEach(modContainer -> {
            modContainer.invokeEntrypoint(key, entrypointType, entrypointInvoker);
        });
    }

}
