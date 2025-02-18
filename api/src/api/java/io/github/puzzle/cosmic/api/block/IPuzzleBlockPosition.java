package io.github.puzzle.cosmic.api.block;

import io.github.puzzle.cosmic.api.world.IPuzzleChunk;
import io.github.puzzle.cosmic.api.world.IPuzzleZone;
import io.github.puzzle.cosmic.util.ApiDeclaration;

@ApiDeclaration(api = IPuzzleBlockPosition.class, impl = "BlockPosition")
public interface IPuzzleBlockPosition {

    int _getLocalX();
    int _getLocalY();
    int _getLocalZ();

    int _getGlobalX();
    int _getGlobalY();
    int _getGlobalZ();

    IPuzzleChunk _getChunk();
    IPuzzleZone _getZone();

    IPuzzleBlockEntity _getBlockEntity();
    IPuzzleBlockEntity _setBlockEntity(IPuzzleBlockState state);

    IPuzzleBlockPosition _set(IPuzzleChunk chunk, int localX, int localY, int localZ);

    void _convertToLocal(IPuzzleZone zone);
    void _setGlobal(IPuzzleZone zone, float x, float y, float z);

    IPuzzleBlockState _getBlockState();
    void _setBlockState(IPuzzleBlockState state);

    int _getSkylight();

}
