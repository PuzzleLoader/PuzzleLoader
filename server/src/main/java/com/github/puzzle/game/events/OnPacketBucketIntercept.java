package com.github.puzzle.game.events;

import com.badlogic.gdx.utils.Array;
import finalforeach.cosmicreach.networking.netty.GamePacket;

public class OnPacketBucketIntercept {

    private Array<GamePacket> bucket;

    public void setPacketBucket(Array<GamePacket> bucket) {
        this.bucket = bucket;
    }

    public Array<GamePacket> getPacketBucket() {
        return bucket;
    }

}
