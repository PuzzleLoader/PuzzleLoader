package com.github.puzzle.core.loader.provider.mod;

import com.github.puzzle.core.loader.meta.ModInfo;
import com.github.puzzle.core.loader.meta.Version;
import com.github.puzzle.core.loader.provider.mod.entrypoint.EntrypointContainer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.zip.ZipFile;

public class ModContainer {
    public ModInfo INFO;
    private final EntrypointContainer entrypointContainer;

    public final String NAME;
    public final String ID;
    public final Version VERSION;
    public final ZipFile ARCHIVE;

    public ModContainer(ModInfo info) {
        this(info, null);
    }

    public ModContainer(@NotNull ModInfo info, ZipFile jar) {
        this.INFO = info;
        this.entrypointContainer = new EntrypointContainer(this, info.Entrypoints);

        NAME = info.DisplayName;
        ID = info.ModID;
        VERSION = info.ModVersion;
        ARCHIVE = jar;
    }

    public <T> void invokeEntrypoint(String key, Class<T> type, Consumer<? super T> invoker) throws Exception {
        entrypointContainer.invokeClasses(key, type, invoker);
    }
}
