package com.github.puzzle.game.events;

import finalforeach.cosmicreach.networking.GamePacket;

public class OnPacketRecieveIntercept {

    private GamePacket packet;

    public void setPacket(GamePacket packet) {
        this.packet = packet;
    }

    public GamePacket getPacket() {
        return packet;
    }

}
