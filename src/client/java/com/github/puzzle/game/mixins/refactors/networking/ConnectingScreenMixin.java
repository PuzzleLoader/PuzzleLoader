package com.github.puzzle.game.mixins.refactors.networking;

import com.github.puzzle.game.networking.packet.cts.CTSIdentificationPacket;
import finalforeach.cosmicreach.accounts.AccountItch;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.puzzle.game.util.IClientNetworkManager.sendAsClient;

@Mixin(AccountItch.class)
public class ConnectingScreenMixin {

    @Inject(method = "handleChallengeAsClient", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/networking/NetworkIdentity;send(Lfinalforeach/cosmicreach/networking/GamePacket;)V", shift = At.Shift.AFTER))
    private void connectToServer(CallbackInfo ci) {
        sendAsClient(new CTSIdentificationPacket("puzzle-loader"));
    }

}
