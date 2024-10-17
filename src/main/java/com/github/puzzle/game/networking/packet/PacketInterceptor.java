package com.github.puzzle.game.networking.packet;

import com.badlogic.gdx.utils.Array;
import com.github.puzzle.game.PuzzleRegistries;
import com.github.puzzle.game.events.OnPacketBucketIntercept;
import com.github.puzzle.game.events.OnPacketIntercept;
import finalforeach.cosmicreach.networking.netty.GamePacket;
import finalforeach.cosmicreach.networking.netty.packets.MessagePacket;
import org.greenrobot.eventbus.Subscribe;

public class PacketInterceptor {

    public static int INTERCEPTED_PACKET_COUNT = 0;
    public static PacketInterceptor INSTANCE;

    public PacketInterceptor() {
        INSTANCE = this;
        PuzzleRegistries.EVENT_BUS.register(this);
    }

    public static void init() {
        new PacketInterceptor();
    }

    public GamePacket modifyPacket(GamePacket packet) {
        INTERCEPTED_PACKET_COUNT++;
        short packetID = packet.packetID;
        String packetClassName = packet.getClass().getName();

        switch (packetClassName) {
            case "finalforeach.cosmicreach.networking.netty.packets.MessagePacket":
                MessagePacket messagePacket = (MessagePacket) packet;
                System.out.println("Recived Msg: " + messagePacket.message + "from: " + messagePacket.playerUniqueId);
                break;
            default:
                break;
        }

        return packet;
    }

    @Subscribe
    public void onEvent(OnPacketIntercept packetSingle) {
        modifyPacket(packetSingle.getPacket());
    }

    @Subscribe
    public void onEvent(OnPacketBucketIntercept packetBucket) {
        Array<GamePacket> bucket = packetBucket.getPacketBucket();
        for (int i = 0; i < bucket.size; i++) {
            modifyPacket(bucket.get(i));
        }
    }

}
