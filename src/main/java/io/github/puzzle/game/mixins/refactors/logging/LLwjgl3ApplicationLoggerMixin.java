package io.github.puzzle.game.mixins.refactors.logging;

import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin(Lwjgl3ApplicationLogger.class)
public class LLwjgl3ApplicationLoggerMixin implements ApplicationLogger {
    @Unique
    private static final Map<String, Logger> LOGGER = new HashMap<>();

    @Unique
    private static Logger logger(String tag) {
        if (LOGGER.containsKey(tag)) return LOGGER.get(tag);
        Logger logger = LoggerFactory.getLogger("CosmicReach | " + tag);
        LOGGER.put(tag, logger);
        return logger;
    }

    @Override
    public void log(String tag, String msg) {
        logger(tag).info(msg);
    }

    @Override
    public void log(String tag, String msg, Throwable throwable) {
        logger(tag).info(msg, throwable);
    }

    @Override
    public void error(String tag, String msg) {
        logger(tag).error(msg);
    }

    @Override
    public void error(String tag, String msg, Throwable throwable) {
        logger(tag).error(msg, throwable);
    }

    @Override
    public void debug(String tag, String msg) {
        logger(tag).debug(msg, msg);
    }

    @Override
    public void debug(String tag, String msg, Throwable throwable) {
        logger(tag).debug(msg, msg, throwable);
    }
}