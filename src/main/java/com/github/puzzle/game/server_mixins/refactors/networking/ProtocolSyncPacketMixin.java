package com.github.puzzle.game.server_mixins.refactors.networking;

import com.badlogic.gdx.utils.ObjectIntMap;
import com.github.puzzle.game.networking.packet.PacketInterceptor;
import finalforeach.cosmicreach.networking.common.NetworkIdentity;
import finalforeach.cosmicreach.networking.netty.GamePacket;
import finalforeach.cosmicreach.networking.netty.packets.meta.ProtocolSyncPacket;
import io.netty.channel.ChannelHandlerContext;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ProtocolSyncPacket.class)
public abstract class ProtocolSyncPacketMixin extends GamePacket {

    public void write() {
        PacketInterceptor.init();

        this.writeInt(packetNamesToIntIds.size - PacketInterceptor.PUZZLE_RESERVED_PACKET_IDS.size());

        for (ObjectIntMap.Entry<String> packetNamesToIntId : packetNamesToIntIds) {
            if (!PacketInterceptor.PUZZLE_RESERVED_PACKET_IDS.contains(packetNamesToIntId.value)) {
                this.writeString(packetNamesToIntId.key);
                this.writeInt(packetNamesToIntId.value);
            }
        }

    }

    @Override
    protected void handle(NetworkIdentity networkIdentity, ChannelHandlerContext channelHandlerContext) {

    }
}
