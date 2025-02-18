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
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockPosition.class)
public class BlockPositionMixin implements IPuzzleBlockPosition {

    @Shadow public int localX;
    @Shadow public int localY;
    @Shadow public int localZ;
    @Shadow public Chunk chunk;
    @Unique
    BlockPosition puzzleLoader$blockPosition = IPuzzleBlockPosition.as(this);

    @Override
    public int _getLocalX() {
        return puzzleLoader$blockPosition.localX();
    }

    @Override
    public int _getLocalY() {
        return puzzleLoader$blockPosition.localY();
    }

    @Override
    public int _getLocalZ() {
        return puzzleLoader$blockPosition.localZ();
    }

    @Override
    public int _getGlobalX() {
        return puzzleLoader$blockPosition.getGlobalX();
    }

    @Override
    public int _getGlobalY() {
        return puzzleLoader$blockPosition.getGlobalY();
    }

    @Override
    public int _getGlobalZ() {
        return puzzleLoader$blockPosition.getGlobalZ();
    }

    @Override
    public IPuzzleChunk _getChunk() {
        return IPuzzleChunk.as(puzzleLoader$blockPosition.chunk());
    }

    @Override
    public IPuzzleZone _getZone() {
        return IPuzzleZone.as(puzzleLoader$blockPosition.getZone());
    }

    @Override
    public IPuzzleBlockEntity _getBlockEntity() {
        return IPuzzleBlockEntity.as(puzzleLoader$blockPosition.getBlockEntity());
    }

    @Override
    public IPuzzleBlockEntity _setBlockEntity(IPuzzleBlockState state) {
        return IPuzzleBlockEntity.as(puzzleLoader$blockPosition.setBlockEntity(state.as()));
    }

    @Override
    public IPuzzleBlockPosition _set(IPuzzleChunk chunk, int localX, int localY, int localZ) {
        return IPuzzleBlockPosition.as(puzzleLoader$blockPosition.set(chunk.as(), localX, localY, localZ));
    }

    @Override
    public void _convertToLocal(IPuzzleZone zone) {
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
    public void _setGlobal(IPuzzleZone zone, float x, float y, float z) {
        this._set(null, (int)Math.floor(x), (int)Math.floor(y), (int)Math.floor(z));
        this._convertToLocal(zone);
    }

    @Override
    public IPuzzleBlockState _getBlockState() {
        return IPuzzleBlockState.as(puzzleLoader$blockPosition.getBlockState());
    }

    @Override
    public void _setBlockState(IPuzzleBlockState state) {
        puzzleLoader$blockPosition.setBlockState((BlockState) state);
    }

    @Override
    public int _getSkylight() {
        return puzzleLoader$blockPosition.getSkyLight();
    }
}
