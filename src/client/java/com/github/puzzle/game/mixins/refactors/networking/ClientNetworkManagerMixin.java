package com.github.puzzle.game.mixins.refactors.networking;

import com.github.puzzle.game.networking.packet.cts.CTSIdentificationPacket;
import com.github.puzzle.game.util.IClientNetworkManager;
import finalforeach.cosmicreach.networking.client.ClientNetworkManager;
import finalforeach.cosmicreach.networking.netty.GamePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientNetworkManager.class)
public abstract class ClientNetworkManagerMixin implements IClientNetworkManager {

    @Override
    public void SIDED_sendAsClient(GamePacket packet) {
        sendAsClient(packet);
    }

    @Override
    public boolean SIDED_isConnected() {
        return isConnected();
    }

    @Shadow
    public static void sendAsClient(GamePacket packet) {
    }

    @Shadow
    public static boolean isConnected() {
        return false;
    }

    @Inject(method = "lambda$connectToServer$0", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/networking/client/ClientNetworkManager;sendAsClient(Lfinalforeach/cosmicreach/networking/netty/GamePacket;)V", shift = At.Shift.AFTER))
    private static void connectToServer(Runnable onConnect, CallbackInfo ci) {
        sendAsClient(new CTSIdentificationPacket("puzzle-loader"));
    }

}
