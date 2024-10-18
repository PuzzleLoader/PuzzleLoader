package com.github.puzzle.game.util;

import com.github.puzzle.core.Constants;
import com.github.puzzle.core.loader.meta.EnvType;
import com.github.puzzle.core.loader.util.Reflection;
import finalforeach.cosmicreach.networking.netty.GamePacket;

public interface IClientNetworkManager {

    private static IClientNetworkManager get() {
        if (Constants.SIDE == EnvType.SERVER) return new IClientNetworkManager() {
            @Override
            public boolean SIDED_isConnected() {
                return false;
            }

            @Override
            public void SIDED_sendAsClient(GamePacket packet) {
            }
        };
        return (IClientNetworkManager) Reflection.newInstance("finalforeach.cosmicreach.networking.client.ClientNetworkManager");
    }

    static boolean isConnected() {
        return get().SIDED_isConnected();
    }

    static void sendAsClient(GamePacket packet) {
        get().SIDED_sendAsClient(packet);
    }

    boolean SIDED_isConnected();
    void SIDED_sendAsClient(GamePacket packet);

}
