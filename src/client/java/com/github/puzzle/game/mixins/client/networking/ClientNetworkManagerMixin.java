package com.github.puzzle.game.mixins.client.networking;

import com.github.puzzle.game.util.IClientNetworkManager;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.networking.client.ClientNetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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

}
