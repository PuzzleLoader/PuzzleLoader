package com.github.puzzle.game.networking;

import com.github.puzzle.core.loader.provider.mod.ModContainer;
import com.github.puzzle.core.loader.util.ModLocator;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.ImmutablePair;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import finalforeach.cosmicreach.networking.common.NetworkIdentity;
import finalforeach.cosmicreach.networking.common.NetworkSide;
import finalforeach.cosmicreach.networking.netty.GamePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

public class STCModlistRequestPacket extends GamePacket {

    public STCModlistRequestPacket() {}

    @Override
    public void receive(ByteBuf byteBuf) {}

    @Override
    public void write() {}

    @Override
    protected void handle(NetworkIdentity identity, ChannelHandlerContext channelHandlerContext) {
        if (identity.getSide() == NetworkSide.CLIENT) {
            List<Pair<String, String>> mods = new ArrayList<>();
            for (ModContainer container : ModLocator.locatedMods.values()) {
                mods.add(new ImmutablePair<>(container.ID, container.VERSION.toString()));
            }

            identity.send(new CTSModlistPacket(mods.toArray(new Pair[0])));
        }
    }
}