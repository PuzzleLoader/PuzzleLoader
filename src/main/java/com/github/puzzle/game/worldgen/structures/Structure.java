package com.github.puzzle.game.worldgen.structures;

import com.github.puzzle.core.Identifier;
import com.github.puzzle.util.Vec3i;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.constants.Direction;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class Structure {

    final short version;
    final Identifier id;

    final List<String> palette;

    // int blocks
    // blocksIndex, chunk x y z, bp x y z
    int[] positions;

    public Structure(
            short version,
            Identifier id
    ) {
        this.palette = new ArrayList<>(0);
        palette.add("base:air[default]");
        this.version = version;
        this.id = id;

        this.positions = new int[16 * 16 * 16];
    }

    Structure(short version, String id, List<String> blocks, int[] positions) {
        this.version = version;
        this.palette = blocks;
        this.id = Identifier.fromString(id);
        this.positions = positions;
    }

    int addBlock(String blockId) {
        if (!palette.contains(blockId)) {
            palette.add(blockId);
            return palette.size() - 1;
        }
        return palette.indexOf(blockId);
    }

    static int to1DCoords(int x, int y, int z) {
        return (z * 16 * 16) + (y * 16) + x;
    }

    static int to1DCoords(Vec3i vec3i) {
        return (vec3i.z() * 16 * 16) + (vec3i.y() * 16) + vec3i.x();
    }

    static Vec3i to3DCoords(int index) {
        int tmpIndex = index;

        int z = tmpIndex / (16 * 16);
        tmpIndex -= (z * 16 * 16);
        int y = tmpIndex / 16;
        int x = tmpIndex % 16;
        return new Vec3i(x, y, z);
    }

    public void setBlockState(BlockState state, int x, int y, int z) {
        positions[to1DCoords(x, y, z)] = addBlock(state.getSaveKey());
    }

    static Map<String, BlockState> blockStateCache = new HashMap<>();

    public BlockState getBlockState(int x, int y, int z) {
        return
                x >= 0 && y >= 0 && z >= 0 && x < 16 && y < 16 && z < 16 ?
                        getInstance(palette.get(positions[to1DCoords(x, y, z)]))
                        : null;
    }

    public void prunePalette() {
        List<String> oldPalette = new ArrayList<>(palette);
        palette.clear();
        for (int i = 0; i < positions.length; i++) {
            palette.add(oldPalette.get(positions[i]));
            positions[i] = palette.size() - 1;
        }
        oldPalette.clear();
    }

    public boolean isEntirely(BlockState state) {
        prunePalette();
        return (palette.size() == 1) && (Objects.equals(palette.get(0), state.getSaveKey()));
    }

    public boolean isEntirely(Predicate<BlockState> statePredicate) {
        for (int idx : positions) {
            BlockState state = getInstance(palette.get(idx));

            if (!statePredicate.test(state)) {
                return false;
            }
        }
        return true;
    }

    public boolean isEntirelyOpaque() {
        return isEntirely((b) -> b != null && b.isOpaque);
    }

    public boolean isEntirelyOneBlockSelfCulling() {
        prunePalette();
        return (palette.size() == 1) && isEntirely((b) -> b != null && b.cullsSelf);
    }

    public boolean isCulledByAdjacentChunks(Vec3i pos, Map<Vec3i, Structure> zone) {
        for (Direction d : Direction.ALL_DIRECTIONS) {
            Structure n = zone.get(new Vec3i(pos.x() + d.getXOffset(), pos.y() + d.getYOffset(), pos.z() + d.getZOffset()));
            if (n == null || !n.isEntirelyOpaque()) {
                return false;
            }
        }

        return true;
    }

    public int getMaxNonEmptyBlockIdxYXZ() {
        return 4096;
    }

    public static BlockState getInstance(String str) {
        if (blockStateCache.containsKey(str)) return blockStateCache.get(str);
        blockStateCache.put(str, BlockState.getInstance(str));
        return blockStateCache.get(str);
    };

    public void foreach(BiConsumer<Vec3i, BlockState> blockConsumer) {
        for (int i = 0; i < positions.length; i++) {
            int idx = positions[i];
            blockConsumer.accept(
                    to3DCoords(i),
                    getInstance(palette.get(idx))
            );
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
        stream.writeInt(structure.palette.size());
        for (String block : structure.palette) stream.writeUTF(block);

        // Write Structure Data
        stream.writeInt(structure.positions.length);
        for (int i = 0; i < structure.positions.length; i++) {
            stream.writeInt(structure.positions[i]);

            Vec3i coords = to3DCoords(i);
            stream.writeByte(coords.x());
            stream.writeByte(coords.y());
            stream.writeByte(coords.z());
        }
    }

}
