package dev.crmodders.puzzle.core.mixins.logging;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(Lwjgl3ApplicationLogger.class)
public class Lwjgl3ApplicationLoggerMixin {

    @Unique
    private static Map<String, Logger> cache = new HashMap<>();

    @Unique
    private static Logger taggedLogger(String tag) {
        if (cache.containsKey(tag)) return cache.get(tag);
        Logger logger = LogManager.getLogger("CR | " + tag);
        cache.put(tag, logger);
        return logger;
    }

    @Inject(method = "log(Ljava/lang/String;Ljava/lang/String;)V", at = @At("HEAD"), cancellable = true)
    public void log(String tag, String msg, CallbackInfo ci) {
        taggedLogger(tag).info(msg);
        ci.cancel();
    }

    @Inject(method = "log(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V", at = @At("HEAD"), cancellable = true)
    public void log(String tag, String msg, Throwable throwable, CallbackInfo ci) {
        taggedLogger(tag).info(msg, throwable);
        ci.cancel();
    }

    @Inject(method = "error(Ljava/lang/String;Ljava/lang/String;)V", at = @At("HEAD"), cancellable = true)
    public void error(String tag, String msg, CallbackInfo ci) {
        taggedLogger(tag).error(msg);
        ci.cancel();
    }

    @Inject(method = "error(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V", at = @At("HEAD"), cancellable = true)
    public void error(String tag, String msg, Throwable throwable, CallbackInfo ci) {
        taggedLogger(tag).error(msg, throwable);
        ci.cancel();
    }

    @Inject(method = "debug(Ljava/lang/String;Ljava/lang/String;)V", at = @At("HEAD"), cancellable = true)
    public void debug(String tag, String msg, CallbackInfo ci) {
        taggedLogger(tag).debug(msg, msg);
        ci.cancel();
    }

    @Inject(method = "debug(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V", at = @At("HEAD"), cancellable = true)
    public void debug(String tag, String msg, Throwable throwable, CallbackInfo ci) {
        taggedLogger(tag).debug(msg, msg, throwable);
        ci.cancel();
    }
}
