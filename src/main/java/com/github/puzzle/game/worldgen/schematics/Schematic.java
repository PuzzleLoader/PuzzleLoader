package com.github.puzzle.game.worldgen.schematics;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.github.puzzle.core.Identifier;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;

import java.io.*;
import java.util.Arrays;

public class Schematic {
    static ArrayMap<String, Schematic> schematic = new ArrayMap<>();
    Array<String> blockStates;
    int[] blockPlacement;
    public int length, height, width;
    public Vector3 origen;

    public Schematic(Array<String> blockStates, int[] blockPlacement, int length, int height, int width) {
        this.blockStates = blockStates;
        this.blockPlacement = blockPlacement;
        origen = new Vector3(0,0,0);
        this.length = length;
        this.height = height;
        this.width = width;
    }

    public Schematic(int length, int height, int width) {
        this.length = length;
        this.height = height;
        this.width = width;
        origen = new Vector3(0,0,0);
        this.blockStates = new Array<>();
        this.blockPlacement = new int[length * height * width];
    }

    public void setBlockState(BlockState blockState, int x, int y, int z){
        int index;
        if(blockStates.contains(blockState.toString(), true))
            index = blockStates.indexOf(blockState.toString(), true);
        else {
            index = blockStates.size;
            blockStates.add(blockState.toString());
        }
        int location = (x * height * width) + (y * width) + z;
        System.out.println(location + " at -->" + new Vector3(x, y, z));
        blockPlacement[location] = index;
    }

    public BlockState getBlockState(int x, int y, int z){
        int location = (x * height * width) + (y * width) + z;
        System.out.println(location + " at -->" + new Vector3(x, y, z));
        return BlockState.getInstance(blockStates.get(blockPlacement[location]));
    }

    public void spawnSchematic(Zone zone, Chunk chunk, int localX, int localY, int localZ) {
        System.out.println("genSchem at:" + new Vector3(localX, localY, localZ));
        for (int x = 0; x < this.length; x++) {
            for (int y = 0; y < this.height; y++) {
                for (int z = 0; z < this.width; z++) {
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

                        BlockState blockState = this.getBlockState(x, y, z);
                        if(blockState == null) continue;
                        adjacentChunk.setBlockState(blockState, bx, by, bz);
                        adjacentChunk.flagForRemeshing(false);
                        continue;
                    }
                    BlockState blockState = this.getBlockState(x, y, z);
                    if(blockState == null) continue;
                    chunk.setBlockState(blockState, calX, calY, calZ);
                    chunk.flagForRemeshing(false);
                }
            }
        }
    }

    public static Schematic read(DataInputStream stream) throws IOException {
        String version = stream.readUTF();
        int length = stream.readInt();
        int height = stream.readInt();
        int width = stream.readInt();
        Vector3 origen = new Vector3(stream.readInt(), stream.readInt(), stream.readInt());
        int blockStatesSize = stream.readInt();
        int blockPlacementLength = length * height * width;
        Array<String> blockStates = new Array<>();
        for (int i = 0; i < blockStatesSize; i++) {
            blockStates.add(stream.readUTF());
        }
        int[] blockPlacement = new int[blockPlacementLength];
        for (int i = 0; i < blockPlacementLength; i++) {
            blockPlacement[i] = stream.readInt();
        }
        Schematic schematic = new Schematic(blockStates, blockPlacement, length, height, width);
        schematic.origen = origen;
        return schematic;
    }

    public void write(DataOutputStream stream) throws IOException {
        stream.writeUTF(VERSION.V0_0_1.versionName);
        stream.writeInt(this.length);
        stream.writeInt(this.height);
        stream.writeInt(this.width);
        stream.writeInt((int)this.origen.x);
        stream.writeInt((int)this.origen.y);
        stream.writeInt((int)this.origen.z);
        stream.writeInt(this.blockStates.size);
        for (String str : this.blockStates) {
            stream.writeUTF(str);
        }
        for(int i: this.blockPlacement){
            stream.writeInt(i);
        }
    }

    public static Schematic getSchematic(String identifier){
        return schematic.get(identifier);
    }

    public static Schematic getSchematic(Identifier identifier){
        return schematic.get(identifier.toString());
    }

    public static void registerSchematic(Identifier identifier, Schematic schem){
        if(schematic.get(identifier.toString()) != null) throw new RuntimeException("Prefabs: " + identifier.toString() + " is already register");
        schematic.put(identifier.toString(), schem);
    }

    public static Schematic loadSchematic(String path) throws IOException {
        File schematicFile = new File(path);
        DataInputStream stream = new DataInputStream(new FileInputStream(schematicFile));
        Schematic loadedSchematic = Schematic.read(stream);
        Identifier identifier = new Identifier("Player", schematicFile.getName());
        Schematic.registerSchematic(identifier, loadedSchematic);
        return loadedSchematic;
    }

    public static void loadSchematicInFolder() throws IOException {
        File folder = new File("schematic");
        File[] files = folder.listFiles();
        if(files == null) return;
        for(File file : files){
            if(!file.isFile() || !file.getName().endsWith(".schematic")) continue;
            loadSchematic(file.getPath());
        }
    }

    public static Schematic generateASchematic(Vector3 pos1, Vector3 pos2, Zone zone){
        boolean isflip = false;
        if(pos1.y < pos2.y) {
            Vector3 newBottomVec = pos1;
            pos1 = pos2;
            pos2 = newBottomVec;
            isflip = true;
        }
        int length = (int)Math.floor(Math.abs(pos2.x - pos1.x));  // Length along x-axis
        int height = (int)Math.floor(Math.abs(pos2.y - pos1.y));  // Height along y-axis
        int width = (int)Math.floor(Math.abs(pos2.z - pos1.z));
        Vector3 findStartingPos = FindStartingPos(pos1, pos2, length, height, width);
        Schematic schematic = new Schematic(length, height, width);
        for (int x = 0; x < schematic.length; x++) {
            for (int y = 0; y < schematic.height; y++) {
                for (int z = 0; z < schematic.width; z++) {
                    int bpx = (int) findStartingPos.x + x;
                    int bpy = (int) findStartingPos.y + y;
                    int bpz = (int) findStartingPos.z + z;
                    BlockState bs = zone.getBlockState(bpx,bpy,bpz);
                    if(bs == null) { System.out.println("NULL"); return null; }
                    schematic.setBlockState(bs,x,y,z);
                }
            }
        }
        return schematic;
    }

    private static Vector3 FindStartingPos(Vector3 pos1, Vector3 pos2, int l, int h, int w){
        Vector3 vec = new Vector3(pos2);
        if(pos2.z > pos1.z && pos2.x < pos1.x) vec.z = pos2.z - w;
        if(pos2.z > pos1.z && pos2.x > pos1.x) {
            vec.z = pos2.z - w;
            vec.x = pos1.x - l;
        }
        if(pos2.z < pos1.z && pos2.x > pos1.x) vec.x = pos2.x - l;
        return vec;
    }

    public static void genSchematicStructureAtLocal(String schematicName, Zone zone, Chunk chunk, int localX, int localY, int localZ){
        Schematic schematic = Schematic.getSchematic(schematicName);
        schematic.spawnSchematic(zone, chunk, localX, localY, localZ);
    }

    public static void genSchematicStructureAtGlobal(String schematicName, Zone zone, Chunk chunk, int globalX, int globalY, int globalZ){
        Schematic schematic = Schematic.getSchematic(schematicName);
        genSchematicStructureAtGlobal(schematic, zone, chunk, globalX, globalY, globalZ);
    }

    public static void genSchematicStructureAtGlobal(Schematic schematic, Zone zone, Chunk chunk, int globalX, int globalY, int globalZ){
        int localX = globalX - chunk.blockX;
        int localY = globalY - chunk.blockY;
        int localZ = globalZ - chunk.blockZ;
        schematic.spawnSchematic(zone, chunk, localX, localY, localZ);
    }

    public enum VERSION {
        V0_0_1("Version 0.0.1");

        private final String versionName;

        VERSION(String versionName){
            this.versionName = versionName;
        }
    }
}
