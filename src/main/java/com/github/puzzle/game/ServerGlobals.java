package com.github.puzzle.game;

import com.github.puzzle.core.loader.launch.Piece;
import com.github.puzzle.core.loader.meta.Env;
import com.github.puzzle.core.loader.meta.EnvType;
import com.github.puzzle.core.loader.util.ModLocator;
import finalforeach.cosmicreach.settings.BooleanSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class ServerGlobals {

    @Env(EnvType.SERVER)
    public static final Logger SERVER_LOGGER = LoggerFactory.getLogger("Puzzle | Server");

    @Env(EnvType.SERVER)
    public static boolean isRunning = false;

    static final AtomicReference<Boolean> paradoxExist = new AtomicReference<>();

    @Env(EnvType.SERVER)
    public static boolean isRunningOnParadox = ((Supplier<Boolean>) () -> {
        if (paradoxExist.get() != null) return paradoxExist.get();
        if (System.getProperty("puzzle.useParadox") != null) {
            paradoxExist.set(true);
            return true;
        }

        try {
            Class.forName(ModLocator.PARADOX_SERVER_ENTRYPOINT, false, Piece.classLoader);
            paradoxExist.set(true);
            return true;
        } catch (ClassNotFoundException ignore) {
            paradoxExist.set(false);
            return false;
        }
    }).get();

    public static boolean GameLoaderHasLoaded;
    public static final BooleanSetting EnabledVanillaMods = new BooleanSetting("enableVanillaMods", true);

    @Env(EnvType.SERVER)
    public static final File SERVER_LOCATION = new File("./");

}