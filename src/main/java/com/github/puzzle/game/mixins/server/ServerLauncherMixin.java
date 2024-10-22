package com.github.puzzle.game.mixins.server;

import com.github.puzzle.core.loader.launch.Piece;
import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.PreModInitializer;
import com.github.puzzle.game.ServerGlobals;
import com.github.puzzle.game.mod.ServerPuzzle;
import finalforeach.cosmicreach.server.ServerLauncher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.net.URI;

@Mixin(ServerLauncher.class)
public class ServerLauncherMixin {

    @Inject(method = "main", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/networking/server/ServerSingletons;create()V", shift = At.Shift.BEFORE))
    private static void preInitLoader(String[] args, CallbackInfo ci) {
        Piece.provider.addBuiltinMods();
        PreModInitializer.invokeEntrypoint();
    }

    @Inject(method = "main", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/networking/netty/NettyServer;run()V", shift = At.Shift.BEFORE))
    private static void consoleInitializer(String[] args, CallbackInfo ci) {
        ServerGlobals.isRunning = true;
        if (!ServerGlobals.isRunningOnParadox()) {
            ServerPuzzle.initConsoleThread();
        }
    }

    @Inject(method = "main", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/networking/netty/NettyServer;run()V", shift = At.Shift.AFTER))
    private static void runningChecker(String[] args, CallbackInfo ci) {
        ServerGlobals.isRunning = false;
    }

    /* Fix Launch URI error */
    @ModifyArg(method = "main", at = @At(value = "INVOKE", target = "Ljava/io/File;<init>(Ljava/net/URI;)V"))
    private static URI fixURI(URI uri) {
        return ServerGlobals.SERVER_LOCATION.toURI();
    }

    @Redirect(method = "main", at = @At(value = "INVOKE", target = "Ljava/io/File;getParentFile()Ljava/io/File;"))
    private static File get(File instance) {
        return instance;
    }

}
