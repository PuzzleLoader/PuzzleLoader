package com.github.puzzle.game;

import finalforeach.cosmicreach.settings.BooleanSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ServerGlobals {

    public static final Logger SERVER_LOGGER = LoggerFactory.getLogger("Puzzle | Server");
    public static boolean isRunning = false;

    public static boolean GameLoaderHasLoaded;
    public static final BooleanSetting EnabledVanillaMods = new BooleanSetting("enableVanillaMods", true);
    public static final File SERVER_LOCATION = new File("./");

}
