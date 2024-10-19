package com.github.puzzle.game;

import com.github.puzzle.core.loader.launch.Piece;
import com.github.puzzle.core.loader.meta.Env;
import com.github.puzzle.core.loader.meta.EnvType;
import com.github.puzzle.core.loader.util.ClassPathFileEntry;
import com.github.puzzle.core.loader.util.ModLocator;
import finalforeach.cosmicreach.settings.BooleanSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

public class ServerGlobals {

    @Env(EnvType.SERVER)
    public static final Logger SERVER_LOGGER = LoggerFactory.getLogger("Puzzle | Server");

    @Env(EnvType.SERVER)
    public static boolean isRunning = false;

    @Env(EnvType.SERVER)
    public static boolean isRunningOnParadox = ((Supplier<Boolean>) () -> {
        if (System.getProperty("puzzle.useParadox") != null) return true;

        ModLocator.forEachEntryOnClasspath(Piece.classLoader.getSources(), entry -> {
            if (entry.isArchive() || entry.isDirectory()) {
                try {
                    for (ClassPathFileEntry entry1 : entry.listAllFiles()) {
                        if (entry1.getName().contains("jarIdentity.txt")) {
                            String s = new String(entry1.getContents());
                            if (s.contains("Puzzle-Paradox-Plugin-Loader")) {
                                System.setProperty("puzzle.useParadox", "true");
                            }
                            break;
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (entry.file().getName().contains("jarIdentity.txt")) {
                String s;
                try {
                    s = new String(entry.getContents());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (s.contains("Puzzle-Paradox-Plugin-Loader")) {
                    System.setProperty("puzzle.useParadox", "true");
                }
            }
        });

        return System.getProperty("puzzle.useParadox") != null;
    }).get();

    public static boolean GameLoaderHasLoaded;
    public static final BooleanSetting EnabledVanillaMods = new BooleanSetting("enableVanillaMods", true);

    @Env(EnvType.SERVER)
    public static final File SERVER_LOCATION = new File("./");

}
