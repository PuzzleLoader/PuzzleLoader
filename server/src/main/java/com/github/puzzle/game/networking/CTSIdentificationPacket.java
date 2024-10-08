package com.github.puzzle.game.networking;

import com.github.puzzle.game.ServerGlobals;
import com.github.puzzle.game.networking.api.IServerIdentity;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.networking.common.NetworkIdentity;
import finalforeach.cosmicreach.networking.common.NetworkSide;
import finalforeach.cosmicreach.networking.netty.GamePacket;
import finalforeach.cosmicreach.networking.server.ServerSingletons;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class CTSIdentificationPacket extends GamePacket {

    String clientName;

    public CTSIdentificationPacket() {}

    public CTSIdentificationPacket(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public void receive(ByteBuf in) {
        clientName = readString(in);
    }

    @Override
    public void write() {
        writeString(clientName);
    }

    @Override
    protected void handle(NetworkIdentity identity, ChannelHandlerContext ctx) {
        if (identity.getSide() == NetworkSide.SERVER) {
            Account account = ServerSingletons.getAccount(identity);

            ((IServerIdentity) identity).setModdedState(clientName, true);
            ServerGlobals.SERVER_LOGGER.info("Account \"{}\" has joined as a modded client, the client being used is identified as \"{}\"", account.getDisplayName(), clientName);
            ServerGlobals.SERVER_LOGGER.info("Getting modlist from player ID: \"{}\", Name: \"{}\"", account.getUniqueId(), account.getDisplayName());

            identity.send(new STCModlistRequestPacket());
        }
    }
}
