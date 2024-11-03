package com.github.puzzle.game.server_mixins.refactors.networking;

import com.github.puzzle.game.PuzzleRegistries;
import com.github.puzzle.game.events.OnPacketSendIntercept;
import finalforeach.cosmicreach.networking.GamePacket;
import io.netty.channel.ChannelHandlerContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GamePacket.class)
public class PacketSendInterceptorMixin {

    @Inject(method = "setupAndSend(Lio/netty/channel/ChannelHandlerContext;)V", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/networking/GamePacket;setupForSend()V", shift = At.Shift.AFTER))
    private void setupAndSend(ChannelHandlerContext ctx, CallbackInfo ci) {
        OnPacketSendIntercept intercept = new OnPacketSendIntercept();
        intercept.setPacket((GamePacket) (Object) this);
        PuzzleRegistries.EVENT_BUS.post(intercept);
    }
}