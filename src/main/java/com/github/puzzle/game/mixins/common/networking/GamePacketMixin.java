package com.github.puzzle.game.mixins.common.networking;

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

    @Inject(method = "registerPacket(Ljava/lang/Class;)V", at = @At("HEAD"), cancellable = true)
    private static <T extends GamePacket> void registerPacket(Class<T> packetClass, CallbackInfo ci) {
        String packetName = packetClass.getCanonicalName();
        if (packetName == null) {
            String reason = "Packet class must have a canonical name";
            if (packetClass.isAnonymousClass()) {
                reason = reason + String.valueOf(packetClass) + " cannot be an anonymous class.";
            } else if (packetClass.isLocalClass()) {
                reason = reason + String.valueOf(packetClass) + " cannot be a local class.";
            } else if (packetClass.isHidden()) {
                reason = reason + String.valueOf(packetClass) + " cannot be a hidden class.";
            }

            throw new RuntimeException(reason);
        } else {
            PacketInterceptor.registerPacketLazy(packetClass);
        }
        ci.cancel();
    }

    @Inject(method = "registerPacket(Ljava/lang/String;ILjava/lang/Class;)V", at = @At("HEAD"), cancellable = true)
    private static <T extends GamePacket> void registerPacket(String strId, int numId, Class<T> packetClass, CallbackInfo ci) {
        PacketInterceptor.registerPacket(strId, numId, packetClass);
        ci.cancel();
    }

    @Inject(method = "registerPackets", at = @At("TAIL"))
    private static void clinit(CallbackInfo ci) {
        PacketInterceptor.registerReservedPacket("identification-packet", 9000, CTSIdentificationPacket.class);
        PacketInterceptor.registerReservedPacket("modlist-request-packet", 9001, STCModlistRequestPacket.class);
        PacketInterceptor.registerReservedPacket("modlist-send-packet", 9002, CTSModlistPacket.class);

        PacketInterceptor.init();
    }

}
