package com.github.puzzle.game.server_mixins.refactors.networking;

import com.github.puzzle.game.networking.packet.PacketInterceptor;
import com.github.puzzle.game.networking.packet.cts.CTSIdentificationPacket;
import com.github.puzzle.game.networking.packet.cts.CTSModlistPacket;
import com.github.puzzle.game.networking.packet.stc.STCModlistRequestPacket;
import finalforeach.cosmicreach.networking.GamePacket;
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
        PacketInterceptor.registerReservedPacket("identification-packet", 9000, CTSIdentificationPacket.class);
        PacketInterceptor.registerReservedPacket("modlist-request-packet", 9001, STCModlistRequestPacket.class);
        PacketInterceptor.registerReservedPacket("modlist-send-packet", 9002, CTSModlistPacket.class);

        PacketInterceptor.init();
    }

}
