package com.github.puzzle.game.server_mixins.refactors.networking;

import com.github.puzzle.game.networking.CTSIdentificationPacket;
import com.github.puzzle.game.networking.CTSModlistPacket;
import com.github.puzzle.game.networking.STCModlistRequestPacket;
import finalforeach.cosmicreach.networking.netty.GamePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GamePacket.class)
public abstract class GamePacketMixin {

    @Shadow
    protected static <T extends GamePacket> void registerPacket(String strId, int numId, Class<T> packetClass) {
    }

    @Inject(method = "registerPackets", at = @At("TAIL"))
    private static void clinit(CallbackInfo ci) {
        registerPacket("identification-packet", 9000, CTSIdentificationPacket.class);
        registerPacket("modlist-request-packet", 9001, STCModlistRequestPacket.class);
        registerPacket("modlist-send-packet", 9002, CTSModlistPacket.class);
    }

}
