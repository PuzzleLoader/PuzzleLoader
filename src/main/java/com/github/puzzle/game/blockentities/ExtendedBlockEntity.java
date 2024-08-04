package com.github.puzzle.game.blockentities;

import com.github.puzzle.annotations.Internal;
import finalforeach.cosmicreach.blockentities.BlockEntity;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.gamestates.InGame;
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
    public BlockPosition position;

    public ExtendedBlockEntity(Zone zone, int globalX, int globalY, int globalZ) {
        super(zone, globalX, globalY, globalZ);
    }

    @Override
    public String getBlockEntityId() {
        return "puzzle:extendedBlockEntity";
    }

    public void read(CRBinDeserializer deserial) {
        int x = deserial.readInt("x", this.position.getGlobalX());
        int y = deserial.readInt("y", this.position.getGlobalY());
        int z = deserial.readInt("z", this.position.getGlobalZ());
        position = new BlockPosition(
                InGame.getLocalPlayer().getZone(InGame.world).getChunkAtBlock(x, y, z),
                x, y, z
        );
    }

    public void write(CRBinSerializer serial) {
        serial.writeString("stringId", this.getBlockEntityId());
        serial.writeInt("x", this.position.getGlobalX());
        serial.writeInt("y", this.position.getGlobalY());
        serial.writeInt("z", this.position.getGlobalZ());
    }

    public int getGlobalX() {
        return this.position.getGlobalX();
    }

    public int getGlobalY() {
        return this.position.getGlobalY();
    }

    public int getGlobalZ() {
        return this.position.getGlobalZ();
    }

    @Internal
    public void initialize(Chunk chunk, int localX, int localY, int localZ) {
        zone = chunk.getZone();
        position = new BlockPosition(chunk, localX, localY, localZ);
    }
}