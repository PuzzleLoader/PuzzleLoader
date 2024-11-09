package com.github.puzzle.game.networking.packet;

import com.github.puzzle.core.Constants;
import com.github.puzzle.core.loader.meta.EnvType;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class NetworkHandler {

    static final Logger LOGGER = LoggerFactory.getLogger("Puzzle | " + Constants.SIDE.name().toUpperCase() + "NetworkHandler");

    static final Map<Class<? extends PuzzlePacket>, PacketHandler> clientHandlers = new HashMap<>();
    static final Map<Class<? extends PuzzlePacket>, PacketHandler> serverHandlers = new HashMap<>();

    public static <T extends PuzzlePacket> void registerPacketHandler(Class<T> packetClass, EnvType type, PacketHandler handler) {
        switch (type) {
            case CLIENT -> {
                if (Constants.SIDE == EnvType.SERVER) throw new RuntimeException("Cannot register a client packet handler on the server for the packet type \"" + packetClass.getName() + "\"");
                clientHandlers.put(packetClass, handler);
            }
            case SERVER -> {
                if (Constants.SIDE == EnvType.CLIENT) throw new RuntimeException("Cannot register a server packet handler on the client for the packet type \"" + packetClass.getName() + "\"");
                serverHandlers.put(packetClass, handler);
            }
            default -> throw new RuntimeException("Not able to use Environment Type " + type.name() + " for packet handler side");
        }
    }

    public interface PacketHandler {

        void onHandle(PuzzlePacket packet, NetworkIdentity identity, ChannelHandlerContext ctx);
    }

}
