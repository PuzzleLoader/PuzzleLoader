package dev.crmodders.puzzle.core.loader.mod;

import dev.crmodders.puzzle.core.loader.entrypoint.EntrypointContainer;
import dev.crmodders.puzzle.core.loader.mod.info.ModInfo;

import java.util.function.Consumer;
import java.util.zip.ZipFile;

public class ModContainer {

    public ModInfo INFO;
    private final EntrypointContainer entrypointContainer;

    public final String NAME;
    public final String ID;
    public final Version VERSION;
    public final ZipFile JAR;

    public ModContainer(ModInfo info) {
        this(info, null);
    }

    public ModContainer(ModInfo info, ZipFile jar) {
        this.INFO = info;
        this.entrypointContainer = new EntrypointContainer(info.Entrypoints);

        NAME = info.DisplayName;
        ID = info.ModID;
        VERSION = info.ModVersion;
        JAR = jar;
    }

    public <T> void invokeEntrypoint(String key, Class<T> type, Consumer<? super T> invoker) {
        entrypointContainer.invokeClasses(key, type, invoker);
    }

}
