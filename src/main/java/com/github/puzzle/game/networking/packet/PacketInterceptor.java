package com.github.puzzle.game.networking.packet;

import com.badlogic.gdx.utils.Array;
import com.github.puzzle.game.PuzzleRegistries;
import com.github.puzzle.game.events.OnPacketBucketRecieveIntercept;
import com.github.puzzle.game.events.OnPacketRecieveIntercept;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.util.logging.Logger;
import meteordevelopment.orbit.EventHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class PacketInterceptor {

    public static Set<Integer> PUZZLE_RESERVED_PACKET_IDS = new HashSet<>();

    public static int INTERCEPTED_PACKET_COUNT = 0;
    public static PacketInterceptor INSTANCE;

    public PacketInterceptor() {
        INSTANCE = this;
        PuzzleRegistries.EVENT_BUS.subscribe(INSTANCE);
    }

    public static <T extends GamePacket> void registerReservedPacket(String strId, int numId, Class<T> packetClass) {
        try {
            Constructor<T> packetConstructor = packetClass.getDeclaredConstructor();
            Supplier<T> supplier = () -> {
                try {
                    return (T) packetConstructor.newInstance();
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                    return null;
                }
            };
            GamePacket.idsToPackets.put(numId, supplier);
            GamePacket.packetsToIntIds.put(packetClass, numId);
            GamePacket.packetNamesToIntIds.put(strId, numId);
            GamePacket.packetNamesToClasses.put(strId, packetClass);
            Logger.info("Registered packet ( id = " + numId + " ):" + strId);

            PUZZLE_RESERVED_PACKET_IDS.add(numId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void init() {
        if (INSTANCE != null)
            new PacketInterceptor();
    }

    public void modifyPacket(GamePacket packet) {
        INTERCEPTED_PACKET_COUNT++;
        short packetID = packet.packetID;
        String packetClassName = packet.getClass().getName();
//
//        switch (packetClassName) {
//            case "finalforeach.cosmicreach.networking.packets.MessagePacket":
//                MessagePacket messagePacket = (MessagePacket) packet;
//                System.out.println("Recived Msg: " + messagePacket.message + "from: " + messagePacket.playerUniqueId);
//                break;
//            default:
//                break;
//        }

    }

    @EventHandler
    public void onEvent(OnPacketRecieveIntercept packetSingle) {
        modifyPacket(packetSingle.getPacket());
    }

    @EventHandler
    public void onEvent(OnPacketBucketRecieveIntercept packetBucket) {
        Array<GamePacket> bucket = packetBucket.getPacketBucket();
        for (int i = 0; i < bucket.size; i++) {
            modifyPacket(bucket.get(i));
        }
    }

}
