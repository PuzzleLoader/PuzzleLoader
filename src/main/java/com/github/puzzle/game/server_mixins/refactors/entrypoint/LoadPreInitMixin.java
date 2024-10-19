package com.github.puzzle.game.server_mixins.refactors.entrypoint;

import com.github.puzzle.core.loader.launch.Piece;
import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.ServerPreModInitializer;
import finalforeach.cosmicreach.server.ServerLauncher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLauncher.class)
public class LoadPreInitMixin {

    @Inject(method = "main", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/networking/server/ServerSingletons;create()V", shift = At.Shift.BEFORE))
    private static void preInit(String[] args, CallbackInfo ci) {
        Piece.provider.addBuiltinMods();
        ServerPreModInitializer.invokeEntrypoint();
    }

}
