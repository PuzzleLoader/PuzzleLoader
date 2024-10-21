package com.github.puzzle.game.mixins.refactors.networking;

import com.github.puzzle.game.networking.packet.cts.CTSIdentificationPacket;
import finalforeach.cosmicreach.gamestates.ConnectingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.puzzle.game.util.IClientNetworkManager.sendAsClient;

@Mixin(ConnectingScreen.class)
public class ConnectingScreenMixin {

    @Inject(method = "lambda$create$1", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/networking/client/ClientNetworkManager;sendAsClient(Lfinalforeach/cosmicreach/networking/GamePacket;)V", shift = At.Shift.AFTER))
    private void connectToServer(CallbackInfo ci) {
        sendAsClient(new CTSIdentificationPacket("puzzle-loader"));
    }

}
