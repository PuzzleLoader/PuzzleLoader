package dev.crmodders.puzzle.mod;

import dev.crmodders.puzzle.entrypoint.EntrypointContainer;

import java.util.function.Consumer;

public class ModContainer {

    public ModInfo extraInfo;
    private final EntrypointContainer entrypointContainer;

    public ModContainer(ModInfo info) {
        this.extraInfo = info;
        this.entrypointContainer = new EntrypointContainer(info.entrypoints());
    }

    public <T> void invokeEntrypoint(String key, Class<T> type, Consumer<? super T> invoker) {
        entrypointContainer.invokeClasses(key, type, invoker);
    }

}
