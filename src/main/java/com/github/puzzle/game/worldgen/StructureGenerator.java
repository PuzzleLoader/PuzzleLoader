package com.github.puzzle.game.worldgen;

import com.badlogic.gdx.utils.ArrayMap;
import com.github.puzzle.core.Identifier;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;

public class StructureGenerator {
    // static list of Structure that are contorted. need
    static ArrayMap<String, Schematic> Prefabs = new ArrayMap<>();

    private Long seed;
    private Zone zone;

    public StructureGenerator(Long seed, Zone zone) {
        this.seed = seed;
        this.zone = zone;
    }

    public void genPrefabStructure(String prefabsName, Chunk chunk, int localX, int localY, int localZ){
        Schematic schem = Prefabs.get(prefabsName);
        for (int y = 0; y < schem.height; y++) {
            for (int x = 0; x < schem.length; x++) {
                for (int z = 0; z < schem.width; z++) {
                    int calX = x + localX;
                    int calY = y + localY;
                    int calZ = z + localZ;
                    if(calX >= Chunk.CHUNK_WIDTH || calY >= Chunk.CHUNK_WIDTH || calZ >= Chunk.CHUNK_WIDTH) {
                        int cx = Math.floorDiv(calX + chunk.blockX, 16);
                        int cy = Math.floorDiv(calY + chunk.blockY, 16);
                        int cz = Math.floorDiv(calZ + chunk.blockZ, 16);
                        int bx = Math.abs((calX + chunk.blockX) - (cx * 16));
                        int by = Math.abs((calY + chunk.blockY) - (cy * 16));
                        int bz = Math.abs((calZ + chunk.blockZ) - (cz * 16));
                        Chunk adjacentChunk = zone.getChunkAtChunkCoords(cx, cy, cz);
                        if (adjacentChunk == null) {
                            adjacentChunk = new Chunk(cx, cy, cz);
                            adjacentChunk.initChunkData();
                            adjacentChunk.setGenerated(true);
                            zone.addChunk(adjacentChunk);
                        }

                        BlockState blockState = schem.getBlockStates(x, y, z);
                        if(blockState == null) continue;
                        adjacentChunk.setBlockState(blockState, bx, by, bz);
                        adjacentChunk.flagForRemeshing(false);
                        continue;
                    }
                    BlockState blockState = schem.getBlockStates(x, y, z);
                    if(blockState == null) continue;
                    chunk.setBlockState(blockState, calX, calY, calZ);
                }
            }
        }
    }

    public static void registerPrefabs(Identifier identifier, Schematic schem){
        if(Prefabs.get(identifier.toString()) != null) throw new RuntimeException("Prefabs: " + identifier.toString() + " is already register");
        Prefabs.put(identifier.toString(), schem);
    }
}
