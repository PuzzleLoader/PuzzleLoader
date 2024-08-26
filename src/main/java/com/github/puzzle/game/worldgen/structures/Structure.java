package com.github.puzzle.game.worldgen.structures;

import com.github.puzzle.core.Identifier;
import com.github.puzzle.util.Vec3i;
import finalforeach.cosmicreach.blocks.BlockState;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Structure {

    final short version;
    final Identifier id;

    final List<String> blocks;

    // int blocks
    // blocksIndex, chunk x y z, bp x y z
    int[] positions;

    public Structure(
            short version,
            Identifier id,
            byte width,
            byte depth,
            byte height
    ) {
        this.blocks = new ArrayList<>(1);
        this.version = version;
        this.id = id;

        this.positions = new int[(width * height) * depth];
    }

    Structure(short version, String id, List<String> blocks, int[] positions) {
        this.version = version;
        this.blocks = blocks;
        this.id = Identifier.fromString(id);
        this.positions = positions;
    }

    int addBlock(String blockId) {
        if (!blocks.contains(blockId)) blocks.add(blockId);
        return blocks.size() - 1;
    }

    static int to1DCoords(int x, int y, int z) {
        return (z * 16 * 16) + (y * 16) + x;
    }

    static int to1DCoords(Vec3i vec3i) {
        return (vec3i.z * 16 * 16) + (vec3i.y * 16) + vec3i.x;
    }

    static Vec3i to3DCoords(int index) {
        int tmpIndex = index;

        int z = tmpIndex / (16 * 16);
        tmpIndex -= (z * 16 * 16);
        int y = tmpIndex / 16;
        int x = tmpIndex % 16;
        return new Vec3i(x, y, z);
    }

    public void addToBlock(BlockState state, int x, int y, int z) {
        positions[to1DCoords(x, y, z)] = addBlock(state.getSaveKey());
    }

    static Map<String, BlockState> blockStateCache = new HashMap<>();

    public void foreach(BiConsumer<Vec3i, @Nullable BlockState> blockConsumer) {
        Function<String, BlockState> getInstance = (str) -> {
            if (blockStateCache.containsKey(str)) return blockStateCache.get(str);
            blockStateCache.put(str, BlockState.getInstance(str));
            return blockStateCache.get(str);
        };

        for (int i = 0; i < positions.length; i++) {
            int idx = positions[i];
            if (idx == 0) {
                blockConsumer.accept(
                        to3DCoords(i),
                        null
                );
            } else {
                blockConsumer.accept(
                        to3DCoords(i),
                        getInstance.apply(blocks.get(idx))
                );
            }
        }
    }

    public static Structure readFromStream(DataInputStream stream) throws IOException {
        // Read Header
        short version = stream.readShort();
        switch (StructureFormat.values()[version]) {
            case BLOCKS_ONLY: return readVersion0(version, stream);
            default: throw new RuntimeException("Invalid structure version");
        }
    }

    static Structure readVersion0(short version, DataInputStream stream) throws IOException {
        String id = stream.readUTF();
        List<String> blocks = new ArrayList<>();

        int blockListSize = stream.readInt();
        blocks.add(null);
        for (int i = 0; i < blockListSize - 1; i++) {
            blocks.add(stream.readUTF());
        }

        int size = stream.readInt();

        int[] bytes = new int[size];
        for (int i = 0; i < size; i++) {
            int index = stream.readInt();

            int bx = stream.readUnsignedByte();
            int by = stream.readUnsignedByte();
            int bz = stream.readUnsignedByte();

            if (index != 0) {
                bytes[to1DCoords(bx, by, bz)] = index;
            }
        }

        return new Structure(version, id, blocks, bytes);
    }

    public void writeToStream(DataOutputStream stream) throws IOException {
        // Write Header
        stream.writeShort(version);
        switch (StructureFormat.values()[version]) {
            case BLOCKS_ONLY: writeVersion0(this, stream);
            default: throw new RuntimeException("Invalid structure version");
        }

    }

    static void writeVersion0(Structure structure, DataOutputStream stream) throws IOException {
        stream.writeUTF(structure.id.toString());

        // Write Block Palette
        stream.writeInt(structure.blocks.size());
        for (String block : structure.blocks) stream.writeUTF(block);

        // Write Structure Data
        stream.writeInt(structure.positions.length);
        for (int i = 0; i < structure.positions.length; i++) {
            stream.writeInt(structure.positions[i]);

            Vec3i coords = to3DCoords(i);
            stream.writeByte(coords.x);
            stream.writeByte(coords.y);
            stream.writeByte(coords.z);
        }
    }

}
