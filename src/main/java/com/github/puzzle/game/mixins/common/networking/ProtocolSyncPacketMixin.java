package com.github.puzzle.game.mixins.common.networking;

import com.badlogic.gdx.utils.ObjectIntMap;
import com.github.puzzle.game.networking.packet.PacketInterceptor;
import finalforeach.cosmicreach.RuntimeInfo;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.networking.NetworkSide;
import finalforeach.cosmicreach.networking.packets.meta.ProtocolSyncPacket;
import finalforeach.cosmicreach.util.logging.Logger;
import io.netty.channel.ChannelHandlerContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ProtocolSyncPacket.class)
public abstract class ProtocolSyncPacketMixin extends GamePacket {

    @Shadow public String gameVersion;

    @Shadow public static Runnable onConnect;

    public void write() {
        PacketInterceptor.init();

        this.writeInt(packetNamesToIntIds.size - PacketInterceptor.PUZZLE_RESERVED_PACKET_IDS.size());

        for (ObjectIntMap.Entry<String> packetNamesToIntId : packetNamesToIntIds) {
            if (!PacketInterceptor.PUZZLE_RESERVED_PACKET_IDS.contains(packetNamesToIntId.value)) {
                this.writeString(packetNamesToIntId.key);
                this.writeInt(packetNamesToIntId.value);
            }
        }

        this.writeString(this.gameVersion);
    }

    @Override
    public void handle(NetworkIdentity identity, ChannelHandlerContext channelHandlerContext) {
        String var10000 = this.getClass().getSimpleName();
        Logger.info("Got " + var10000 + " with game version: " + this.gameVersion);
        if (identity.getSide() != NetworkSide.SERVER) {
            identity.send(new ProtocolSyncPacket(RuntimeInfo.version));
            onConnect.run();
        }
    }
}
