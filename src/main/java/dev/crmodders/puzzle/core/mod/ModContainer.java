package dev.crmodders.puzzle.core.mod;

import dev.crmodders.puzzle.core.entrypoint.EntrypointContainer;
import net.minecraft.launchwrapper.IClassTransformer;

import java.util.function.Consumer;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

public class ModContainer {

    public ModJsonInfo JSON_INFO;
    private final EntrypointContainer entrypointContainer;

    public final String NAME;
    public final String ID;
    public final Version VERSION;
    public final ZipFile JAR;

    public ModContainer(ModJsonInfo info) {
        this.JSON_INFO = info;
        this.entrypointContainer = new EntrypointContainer(info.entrypoints());

        NAME = info.name();
        ID = info.id();
        VERSION = Version.parseVersion(info.version());
        JAR = null;
    }

    public ModContainer(ModJsonInfo info, ZipFile jar) {
        this.JSON_INFO = info;
        this.entrypointContainer = new EntrypointContainer(info.entrypoints());

        NAME = info.name();
        ID = info.id();
        VERSION = Version.parseVersion(info.version());
        JAR = jar;
    }

    public <T> void invokeEntrypoint(String key, Class<T> type, Consumer<? super T> invoker) {
        entrypointContainer.invokeClasses(key, type, invoker);
    }

}
