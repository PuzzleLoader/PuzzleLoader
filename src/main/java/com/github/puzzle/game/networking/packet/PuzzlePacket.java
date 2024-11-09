package com.github.puzzle.game.networking.packet;

import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public abstract class PuzzlePacket extends GamePacket {

    @Override
    public void receive(ByteBuf byteBuf) {
        fromNetwork(byteBuf);
    }

    @Override
    public void write() {
        toNetwork();
    }

    @Override
    public void handle(NetworkIdentity networkIdentity, ChannelHandlerContext channelHandlerContext) {
        if (networkIdentity.isClient() && NetworkHandler.clientHandlers.containsKey(this.getClass()))
            NetworkHandler.clientHandlers.get(this.getClass()).onHandle(this, networkIdentity, channelHandlerContext);
        if (networkIdentity.isServer() && NetworkHandler.serverHandlers.containsKey(this.getClass()))
            NetworkHandler.serverHandlers.get(this.getClass()).onHandle(this, networkIdentity, channelHandlerContext);
    }

    public abstract void fromNetwork(ByteBuf byteBuf);
    public abstract void toNetwork();
}
