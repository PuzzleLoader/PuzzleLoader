package com.github.puzzle.game;

import com.github.puzzle.core.loader.launch.Piece;
import com.github.puzzle.core.loader.meta.Env;
import com.github.puzzle.core.loader.meta.EnvType;
import com.github.puzzle.core.loader.util.ModLocator;
import com.github.puzzle.game.engine.ILoadingEngine;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public class ServerGlobals {

    public static ILoadingEngine ENGINE;

    @Env(EnvType.SERVER)
    public static boolean isRunning = false;

    @Env(EnvType.SERVER)
    static final AtomicReference<Boolean> paradoxExist = new AtomicReference<>();

    @Env(EnvType.SERVER)
    public static boolean isRunningOnParadox() {
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
    }

    @Env(EnvType.SERVER)
    public static final File SERVER_LOCATION = new File("./");

}