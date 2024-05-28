package dev.crmodders.puzzle.game.mixins.logging;

import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin(Lwjgl3ApplicationLogger.class)
public class Lwjgl3ApplicationLoggerMixin implements ApplicationLogger {

    @Unique
    private static Map<String, Logger> cache = new HashMap<>();

    @Unique
    private static Logger taggedLogger(String tag) {
        if (cache.containsKey(tag)) return cache.get(tag);
        Logger logger = LogManager.getLogger("CR | " + tag);
        cache.put(tag, logger);
        return logger;
    }

    @Overwrite
    public void log(String tag, String msg) {
        taggedLogger(tag).info(msg);
    }

    @Overwrite
    public void log(String tag, String msg, Throwable throwable) {
        taggedLogger(tag).info(msg, throwable);
    }

    @Overwrite
    public void error(String tag, String msg) {
        taggedLogger(tag).error(msg);
    }

    @Overwrite
    public void error(String tag, String msg, Throwable throwable) {
        taggedLogger(tag).error(msg, throwable);
    }

    @Overwrite
    public void debug(String tag, String msg) {
        taggedLogger(tag).debug(msg, msg);
    }

    @Overwrite
    public void debug(String tag, String msg, Throwable throwable) {
        taggedLogger(tag).debug(msg, msg, throwable);
    }
}
