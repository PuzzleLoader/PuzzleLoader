package com.github.puzzle.game.networking.packet.cts;

import com.github.puzzle.core.loader.util.ModLocator;
import com.github.puzzle.game.ServerGlobals;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.ImmutablePair;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.networking.common.NetworkIdentity;
import finalforeach.cosmicreach.networking.common.NetworkSide;
import finalforeach.cosmicreach.networking.netty.GamePacket;
import finalforeach.cosmicreach.networking.netty.packets.meta.DisconnectPacket;
import finalforeach.cosmicreach.networking.server.ServerSingletons;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.*;

public class CTSModlistPacket extends GamePacket {

    Pair<String, String>[] modList;

    public CTSModlistPacket() {}

    public CTSModlistPacket(Pair<String, String>[] modList) {
        this.modList = modList;
    }

    @Override
    public void receive(ByteBuf in) {
        int listLength = readInt(in);

        modList = new Pair[listLength];

        for (int i = 0; i < listLength; i++) {
            String modId = readString(in);
            String modName = readString(in);

            modList[i] = new ImmutablePair<>(modName, modId);
        }

    }

    @Override
    public void write() {
        writeInt(modList.length);
        for (Pair<String, String> modPair : modList) {
            writeString(modPair.getLeft());
            writeString(modPair.getRight());
        }
    }

    @Override
    protected void handle(NetworkIdentity identity, ChannelHandlerContext ctx) {
        if (identity.getSide() == NetworkSide.SERVER) {
            Set<String> keys = ModLocator.locatedMods.keySet();

            List<Pair<String, String>> missingMods = new ArrayList<>();
            if (identity.getSide() == NetworkSide.SERVER) {
                for (Pair<String, String> modPair : modList) {
                    String modId = modPair.getRight();
                    String modVersion = modPair.getLeft();

                    if (!keys.contains(modId)) {
                        missingMods.add(modPair);
                    }
                    else if (keys.contains(modId) && !Objects.equals(ModLocator.locatedMods.get(modId).VERSION.toString(), modVersion)) {
                        missingMods.add(modPair);
                    }
                }
            }

            if (!missingMods.isEmpty()) {
                Account account = ServerSingletons.getAccount(identity);

                ServerGlobals.SERVER_LOGGER.info("Disconnecting player ID: \"{}\", Name: \"{}\" due to modlist not being the same.", account.getUniqueId(), account.getDisplayName());
                ServerSingletons.server.broadcastAsServerExcept(new DisconnectPacket(account), identity);
                ctx.close();
            }
        }
    }
}