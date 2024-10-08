package com.github.puzzle.game.server_mixins.bugfixes;

import com.github.puzzle.game.ServerGlobals;
import finalforeach.cosmicreach.server.ServerLauncher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.File;
import java.net.URI;
import java.net.URL;

@Mixin(ServerLauncher.class)
public class ServerLocationFixer {

    @Redirect(method = "main", at = @At(value = "INVOKE", target = "Ljava/net/URL;toURI()Ljava/net/URI;"))
    private static URI toURI(URL instance) {
        return ServerGlobals.SERVER_LOCATION.toURI();
    }

    @Redirect(method = "main", at = @At(value = "INVOKE", target = "Ljava/io/File;getParentFile()Ljava/io/File;"))
    private static File getParentFile(File instance) {
        return instance;
    }

}
