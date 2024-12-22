package com.github.puzzle.game.networking.packet;

import com.badlogic.gdx.utils.Array;
import com.github.puzzle.core.loader.util.AnsiColours;
import com.github.puzzle.game.PuzzleRegistries;
import com.github.puzzle.game.events.OnPacketBucketRecieveIntercept;
import com.github.puzzle.game.events.OnPacketRecieveIntercept;
import finalforeach.cosmicreach.networking.GamePacket;
import meteordevelopment.orbit.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class PacketInterceptor {

    static Logger LOGGER = LoggerFactory.getLogger("PacketInterceptor");

    public static Set<Integer> PUZZLE_RESERVED_PACKET_IDS = new HashSet<>();

    public static int INTERCEPTED_PACKET_COUNT = 0;
    public static PacketInterceptor INSTANCE;

    public PacketInterceptor() {
        INSTANCE = this;
        PuzzleRegistries.EVENT_BUS.subscribe(INSTANCE);
    }

    public static <T extends GamePacket> void registerPacketLazy(Class<T> packetClass) {
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

            int numId = GamePacket.idsToPackets.size + 1;
            GamePacket.idsToPackets.put(numId, supplier);
            GamePacket.packetsToIntIds.put(packetClass, numId);
            GamePacket.packetNamesToIntIds.put(packetClass.getName(), numId);
            GamePacket.packetNamesToClasses.put(packetClass.getName(), packetClass);
            LOGGER.info("Registered {}Lazy{} Packet {}\"{}\"{} with numeral ID {}#{}", AnsiColours.BRIGHT_GREEN, AnsiColours.RESET, AnsiColours.BLUE, packetClass.getName(), AnsiColours.RESET, AnsiColours.PURPLE, numId);

            PUZZLE_RESERVED_PACKET_IDS.add(numId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends GamePacket> void registerPacketLazy(String strId, Class<T> packetClass) {
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

            int numId = GamePacket.idsToPackets.size + 1;
            GamePacket.idsToPackets.put(numId, supplier);
            GamePacket.packetsToIntIds.put(packetClass, numId);
            GamePacket.packetNamesToIntIds.put(strId, numId);
            GamePacket.packetNamesToClasses.put(strId, packetClass);
            LOGGER.info("Registered {}Lazy{} Packet {}\"{}\"{} with numeral ID {}#{}", AnsiColours.BRIGHT_GREEN, AnsiColours.RESET, AnsiColours.BLUE, strId, AnsiColours.RESET, AnsiColours.PURPLE, numId);

            PUZZLE_RESERVED_PACKET_IDS.add(numId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends GamePacket> void registerPacket(String strId, int numId, Class<T> packetClass) {
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
            LOGGER.info("Registered {}Regular{} Packet {}\"{}\"{} with numeral ID {}#{}", AnsiColours.BRIGHT_YELLOW, AnsiColours.RESET, AnsiColours.BLUE, strId, AnsiColours.RESET, AnsiColours.PURPLE, numId);

            PUZZLE_RESERVED_PACKET_IDS.add(numId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
            LOGGER.info("Registered {}Reserved{} Packet {}\"{}\"{} with numeral ID {}#{}", AnsiColours.BRIGHT_PURPLE, AnsiColours.RESET, AnsiColours.BLUE, strId, AnsiColours.RESET, AnsiColours.PURPLE, numId);

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
