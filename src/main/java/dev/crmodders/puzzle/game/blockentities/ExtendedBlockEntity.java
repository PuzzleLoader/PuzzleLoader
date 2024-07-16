package dev.crmodders.puzzle.game.blockentities;

import dev.crmodders.flux.tags.Identifier;
import dev.crmodders.puzzle.annotations.Internal;
import finalforeach.cosmicreach.blockentities.BlockEntity;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.world.Chunk;

public class ExtendedBlockEntity extends BlockEntity {

    public Identifier blockEntityId;
    public BlockPosition position;

    public ExtendedBlockEntity(Identifier id, int globalX, int globalY, int globalZ) {
        super(globalX, globalY, globalZ);
        this.blockEntityId = id;
    }

    @Override
    public String getBlockEntityId() {
        return blockEntityId.toString();
    }

    @Internal
    public void initialize(Chunk chunk, int localX, int localY, int localZ) {
        position = new BlockPosition(chunk, localX, localY, localZ);
    }

}