package com.github.puzzle.game.mixins.refactors.networking;

import com.badlogic.gdx.utils.Array;
import com.github.puzzle.game.PuzzleRegistries;
import com.github.puzzle.game.events.OnPacketBucketIntercept;
import com.github.puzzle.game.events.OnPacketIntercept;
import com.llamalad7.mixinextras.sugar.Local;
import finalforeach.cosmicreach.networking.netty.GamePacket;
import finalforeach.cosmicreach.networking.netty.NettyPacketHandler;
import io.netty.channel.ChannelHandlerContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NettyPacketHandler.class)
public class PacketInterceptorMixin {

    @Shadow private Array<GamePacket> bundledPackets;

    @Inject(method = "channelRead", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/networking/netty/GamePacket;handle(Lfinalforeach/cosmicreach/networking/common/NetworkIdentity;Lio/netty/channel/ChannelHandlerContext;)V", ordinal = 0, shift = At.Shift.BEFORE))
    private void channelRead0(ChannelHandlerContext ctx, Object msg, CallbackInfo ci, @Local GamePacket packet) {
        OnPacketIntercept intercept = new OnPacketIntercept();
        intercept.setPacket(packet);
        PuzzleRegistries.EVENT_BUS.post(intercept);
    }

    @Inject(method = "channelRead", at = @At(value = "INVOKE", target = "Lcom/badlogic/gdx/utils/Array;iterator()Lcom/badlogic/gdx/utils/Array$ArrayIterator;", shift = At.Shift.BEFORE))
    private void channelRead1(ChannelHandlerContext ctx, Object msg, CallbackInfo ci) {
        OnPacketBucketIntercept intercept = new OnPacketBucketIntercept();
        intercept.setPacketBucket(bundledPackets);
        PuzzleRegistries.EVENT_BUS.post(intercept);
    }

}
