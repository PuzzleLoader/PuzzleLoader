package dev.crmodders.puzzle.utils;

import dev.crmodders.puzzle.mod.ModLocator;

import java.util.function.Consumer;

public interface EntrypointUtil {

    static void initModsForCollection() {

    }

    static <T> void invoke(String key, Class<T> entrypointType, Consumer<? super T> entrypointInvoker) {
        if (ModLocator.LocatedMods == null) ModLocator.getMods();


    }

}
