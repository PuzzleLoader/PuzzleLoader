package com.github.puzzle.game.networking.packet.cts;

import com.github.puzzle.core.loader.meta.Version;
import com.github.puzzle.core.loader.meta.parser.SideRequire;
import com.github.puzzle.core.loader.provider.mod.ModContainer;
import com.github.puzzle.core.loader.util.ModLocator;
import com.github.puzzle.game.ServerGlobals;
import com.github.puzzle.game.networking.api.IServerIdentity;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.networking.NetworkSide;
import finalforeach.cosmicreach.networking.server.ServerSingletons;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.*;

public class CTSModlistPacket extends GamePacket {

    Pair<String, String>[] modList;
    Map<String, Version> listOfMods;

    public CTSModlistPacket() {}

    public CTSModlistPacket(Pair<String, String>[] modList) {
        this.modList = modList;
    }

    @Override
    public void receive(ByteBuf in) {
        int listLength = readInt(in);

        listOfMods = new HashMap();

        for (int i = 0; i < listLength; i++) {
            String modId = readString(in);
            String modVersion = readString(in);

            listOfMods.put(modId, Version.parseVersion(modVersion));
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
    public void handle(NetworkIdentity identity, ChannelHandlerContext ctx) {
        if (identity.getSide() == NetworkSide.SERVER) {

            List<ModContainer> missingMods = new ArrayList<>();
            Set<String> clientsMods = listOfMods.keySet();
            for (ModContainer mod : ModLocator.locatedMods.values()) {
                String modId = mod.ID;
                Version modVersion = mod.VERSION;
                SideRequire allowedSides = mod.INFO.allowedSides;

                if (allowedSides.isBothRequired()) {
                    if (!clientsMods.contains(modId)) {
                        missingMods.add(mod);
                    } else {
                        switch (modVersion.otherIs(modVersion)) {
                            case LARGER, SMALLER -> missingMods.add(mod);
                        }
                    }
                }
            }

            if (!missingMods.isEmpty()) {
                Account account = ServerSingletons.getAccount(identity);

                StringBuilder missingModsTxt = new StringBuilder();
                String errTxt = "These mods either don't exist or are the wrong version:";
                missingModsTxt.append(errTxt).append("\n");
                for (ModContainer mod : missingMods) {
                    StringBuilder modErrString = new StringBuilder();
                    modErrString.append(mod.NAME).append(": ").append(mod.VERSION);

                    modErrString.insert(0, "> ");
                    for (int i = 0; i < (errTxt.length() - 2) - modErrString.length(); i++) {
                        modErrString.insert(0, "-");
                    }


                    missingModsTxt.append(modErrString).append("\n");
                }

                ServerGlobals.SERVER_LOGGER.info("Disconnecting player ID: \"{}\", Name: \"{}\" due to modlist not being the same.", account.getUniqueId(), account.getDisplayName());
                ServerSingletons.SERVER.kick(missingModsTxt.toString(), identity);
                ctx.close();
            }
            ((IServerIdentity) identity).setModList(listOfMods);
        }
    }
}
