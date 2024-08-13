package com.github.puzzle.game.blockentities;

import com.github.puzzle.annotations.Internal;
import finalforeach.cosmicreach.blockentities.BlockEntity;
import finalforeach.cosmicreach.io.CRBinDeserializer;
import finalforeach.cosmicreach.io.CRBinSerializer;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;

/**
 * @see BlockEntity
 * 
 */
public class ExtendedBlockEntity extends BlockEntity {
    public Zone zone;
    public int x;
    public int y;
    public int z;

    public ExtendedBlockEntity(Zone zone, int globalX, int globalY, int globalZ) {
        super(zone, globalX, globalY, globalZ);
        this.x = globalX;
        this.y = globalY;
        this.z = globalZ;
    }

    @Override
    public String getBlockEntityId() {
        return "puzzle:extendedBlockEntity";
    }

    public void read(CRBinDeserializer deserial) {
        x = deserial.readInt("x", x);
        y = deserial.readInt("y", y);
        z = deserial.readInt("z", z);
    }

    public void write(CRBinSerializer serial) {
        serial.writeString("stringId", this.getBlockEntityId());
        serial.writeInt("x", x);
        serial.writeInt("y", y);
        serial.writeInt("z", z);
    }

    public int getGlobalX() {
        return x;
    }

    public int getGlobalY() {
        return y;
    }

    public int getGlobalZ() {
        return z;
    }

    @Internal
    public void initialize(Chunk chunk, int localX, int localY, int localZ) {
        zone = chunk.getZone();
        x = chunk.blockX + localX;
        y = chunk.blockY + localY;
        z = chunk.blockZ + localZ;
    }
}