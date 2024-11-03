package com.github.puzzle.game.events;

import finalforeach.cosmicreach.networking.GamePacket;

public class OnPacketSendIntercept {

    private GamePacket packet;

    public void setPacket(GamePacket packet) {
        this.packet = packet;
    }

    public GamePacket getPacket() {
        return packet;
    }

}
