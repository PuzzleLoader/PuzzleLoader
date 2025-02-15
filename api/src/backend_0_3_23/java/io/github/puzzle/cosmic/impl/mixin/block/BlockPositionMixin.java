package io.github.puzzle.cosmic.impl.mixin.block;

import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.constants.Direction;
import finalforeach.cosmicreach.world.Chunk;
import io.github.puzzle.cosmic.api.block.IPuzzleBlockEntity;
import io.github.puzzle.cosmic.api.block.IPuzzleBlockPosition;
import io.github.puzzle.cosmic.api.block.IPuzzleBlockState;
import io.github.puzzle.cosmic.api.world.IPuzzleChunk;
import io.github.puzzle.cosmic.api.world.IPuzzleZone;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockPosition.class)
public class BlockPositionMixin implements IPuzzleBlockPosition {

    @Shadow private int localX;
    @Shadow private int localY;
    @Shadow private int localZ;
    @Shadow private Chunk chunk;
    BlockPosition blockPosition = (BlockPosition) (Object) this;

    @Override
    public int getLocalX() {
        return blockPosition.localX();
    }

    @Override
    public int getLocalY() {
        return blockPosition.localY();
    }

    @Override
    public int getLocalZ() {
        return blockPosition.localZ();
    }

    @Override
    public int getGlobalX() {
        return blockPosition.getGlobalX();
    }

    @Override
    public int getGlobalY() {
        return blockPosition.getGlobalY();
    }

    @Override
    public int getGlobalZ() {
        return blockPosition.getGlobalZ();
    }

    @Override
    public IPuzzleChunk getChunk() {
        return (IPuzzleChunk) blockPosition.chunk();
    }

    @Override
    public IPuzzleZone getZone() {
        return (IPuzzleZone) blockPosition.getZone();
    }

    @Override
    public IPuzzleBlockEntity getBlockEntity() {
        return (IPuzzleBlockEntity) blockPosition.getBlockEntity();
    }

    @Override
    public IPuzzleBlockEntity setBlockEntity(IPuzzleBlockState state) {
        return (IPuzzleBlockEntity) blockPosition.setBlockEntity(state.as());
    }

    @Override
    public IPuzzleBlockPosition set(IPuzzleChunk chunk, int localX, int localY, int localZ) {
        return (IPuzzleBlockPosition) blockPosition.set(chunk.as(), localX, localY, localZ);
    }

    @Override
    public void convertToLocal(IPuzzleZone zone) {
        if (this.chunk != null) {
            throw new RuntimeException("This block position is already in local coordinates!");
        } else {
            int globalX = this.localX;
            int globalY = this.localY;
            int globalZ = this.localZ;
            Chunk chunk = zone.as().getChunkAtBlock(globalX, globalY, globalZ);
            int chunkX = Math.floorDiv(globalX, 16);
            int chunkY = Math.floorDiv(globalY, 16);
            int chunkZ = Math.floorDiv(globalZ, 16);
            if (chunk == null) {
                for(Direction d : Direction.ALL_DIRECTIONS) {
                    Chunk n = zone.as().getChunkAtChunkCoords(chunkX + d.getXOffset(), chunkY + d.getYOffset(), chunkZ + d.getZOffset());
                    if (n != null) {
                        chunk = zone.as().createBlankChunk(chunkX, chunkY, chunkZ);
                        break;
                    }
                }
            }

            this.chunk = chunk;
            this.localX = globalX - chunkX * 16;
            this.localY = globalY - chunkY * 16;
            this.localZ = globalZ - chunkZ * 16;
        }
    }

    @Override
    public void setGlobal(IPuzzleZone zone, float x, float y, float z) {
        this.set(null, (int)Math.floor(x), (int)Math.floor(y), (int)Math.floor(z));
        this.convertToLocal(zone);
    }

    @Override
    public IPuzzleBlockState getBlockState() {
        return (IPuzzleBlockState) blockPosition.getBlockState();
    }

    @Override
    public void setBlockState(IPuzzleBlockState state) {
        blockPosition.setBlockState((BlockState) state);
    }

    @Override
    public int getSkylight() {
        return blockPosition.getSkyLight();
    }

    @Override
    public BlockPosition as() {
        return blockPosition;
    }
}
