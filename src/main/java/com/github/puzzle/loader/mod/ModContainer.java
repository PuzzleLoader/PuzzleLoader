package com.github.puzzle.loader.mod;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.puzzle.game.ui.ModMenu;
import com.github.puzzle.loader.entrypoint.EntrypointContainer;
import com.github.puzzle.loader.entrypoint.interfaces.ModInitializer;
import com.github.puzzle.loader.mod.info.ModInfo;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.zip.ZipFile;

public class ModContainer {
    public ModInfo INFO;
    private final EntrypointContainer entrypointContainer;

    public final String NAME;
    public final String ID;
    public final Version VERSION;
    public final ZipFile JAR;
    public ModInitializer modInitializer;

    public ModContainer(ModInfo info) {
        this(info, null);
    }

    public ModContainer(@NotNull ModInfo info, ZipFile jar) {
        this.INFO = info;
        this.entrypointContainer = new EntrypointContainer(this, info.Entrypoints);

        NAME = info.DisplayName;
        ID = info.ModID;
        VERSION = info.ModVersion;
        JAR = jar;
    }

    public <T> void invokeEntrypoint(String key, Class<T> type, Consumer<? super T> invoker) throws Exception {
        entrypointContainer.invokeClasses(key, type, invoker);
    }
}
