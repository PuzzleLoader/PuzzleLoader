package io.github.puzzle.cosmic.api.block;

import io.github.puzzle.cosmic.api.world.IPuzzleChunk;
import io.github.puzzle.cosmic.api.world.IPuzzleZone;
import io.github.puzzle.cosmic.util.ChangeType;

public interface IPuzzleBlockPosition {

    int getLocalX();
    int getLocalY();
    int getLocalZ();

    int getGlobalX();
    int getGlobalY();
    int getGlobalZ();

    IPuzzleChunk getChunk();
    IPuzzleZone getZone();

    IPuzzleBlockEntity getBlockEntity();
    IPuzzleBlockEntity setBlockEntity(IPuzzleBlockState state);

    IPuzzleBlockPosition set(IPuzzleChunk chunk, int localX, int localY, int localZ);

    void convertToLocal(IPuzzleZone zone);
    void setGlobal(IPuzzleZone zone, float x, float y, float z);

    IPuzzleBlockState getBlockState();
    void setBlockState(IPuzzleBlockState state);

    int getSkylight();

    @ChangeType("BlockPosition")
    Object as();

}
