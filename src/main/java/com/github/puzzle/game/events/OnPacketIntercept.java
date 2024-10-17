package com.github.puzzle.game.events;

import finalforeach.cosmicreach.networking.netty.GamePacket;

public class OnPacketIntercept {

    private GamePacket packet;

    public void setPacket(GamePacket packet) {
        this.packet = packet;
    }

    public GamePacket getPacket() {
        return packet;
    }

}
