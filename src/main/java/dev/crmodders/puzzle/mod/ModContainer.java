package dev.crmodders.puzzle.mod;

import dev.crmodders.puzzle.entrypoint.EntrypointContainer;

import java.util.function.Consumer;

public class ModContainer {

    public ModJsonInfo JSON_INFO;
    private final EntrypointContainer entrypointContainer;

    public final String NAME;
    public final String ID;
    public final Version VERSION;

    public ModContainer(ModJsonInfo info) {
        this.JSON_INFO = info;
        this.entrypointContainer = new EntrypointContainer(info.entrypoints());

        NAME = info.name();
        ID = info.id();
        VERSION = Version.parseVersion(info.version());
    }

    public <T> void invokeEntrypoint(String key, Class<T> type, Consumer<? super T> invoker) {
        entrypointContainer.invokeClasses(key, type, invoker);
    }

}
