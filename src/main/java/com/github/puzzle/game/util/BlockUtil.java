package com.github.puzzle.game.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector4;
import com.badlogic.gdx.utils.Queue;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.constants.Direction;
import finalforeach.cosmicreach.lighting.BlockLightPropagator;
import finalforeach.cosmicreach.networking.packets.blocks.BlockReplacePacket;
import finalforeach.cosmicreach.networking.packets.blocks.PlaceBlockPacket;
import finalforeach.cosmicreach.networking.server.ServerSingletons;
import finalforeach.cosmicreach.world.BlockSetter;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;
import finalforeach.cosmicreach.worldgen.ChunkColumn;

public class BlockUtil {

    public static class BlockReplaceSettings {
        public static final BlockReplaceSettings DEFAULT = new BlockReplaceSettings();

        boolean doRemeshing = true;
        boolean doBlockLightPropagation = true;
        private final boolean isImmutable;

        private BlockReplaceSettings(boolean isImmutable) {
            this.isImmutable = isImmutable;
        }

        public BlockReplaceSettings() {
            isImmutable = false;
        }

        public BlockReplaceSettings doesRemeshing(boolean doRemeshing) {
            if (isImmutable) throw new RuntimeException("Cannot set \"doesRemeshing\" on the immutable BlockReplaceSetting");

            this.doRemeshing = doRemeshing;
            return this;
        }

        public BlockReplaceSettings doesBlockLightPropagation(boolean doBlockLightPropagation) {
            if (isImmutable) throw new RuntimeException("Cannot set \"doesBlockLightPropagation\" on the immutable BlockReplaceSetting");

            this.doBlockLightPropagation = doBlockLightPropagation;
            return this;
        }

    }

    private static final Queue<BlockPosition> tmpQueue = new Queue<>();

    public static void setBlockAt(Zone zone, BlockState state, BlockPosition pos, BlockReplaceSettings settings) {
        if (IClientNetworkManager.isConnected()) {
            IClientNetworkManager.sendAsClient(new PlaceBlockPacket(zone, pos, state));
        }

        getChunkAtVec(zone, pos.getGlobalX(), pos.getGlobalY(), pos.getGlobalZ());

        BlockState oldBlockState = pos.getBlockState();
        if (state != oldBlockState) {
            pos.setBlockState(state);
            if (settings.doBlockLightPropagation) {
                adjustLightsAfterReplace(zone, oldBlockState, state, pos, tmpQueue);
            }
            if (settings.doRemeshing) {
                if (state.getModel() != oldBlockState.getModel() && GameSingletons.isClient) {
                    pos.chunk().flagTouchingChunksForRemeshing(zone, pos.localX(), pos.localY(), pos.localZ(), true);
                    GameSingletons.meshGenThread.requestImmediateResorting();
                }
            }
        }

        if (GameSingletons.isHost && ServerSingletons.SERVER != null) {
            ServerSingletons.SERVER.broadcast(zone, new BlockReplacePacket(zone, state, pos));
        }
    }

    private static void adjustLightsAfterReplace(Zone zone, BlockState oldBlockState, BlockState targetBlockState, BlockPosition blockPos, Queue<BlockPosition> tmpQueue) {
        int oldSkylightAttenuation = 0;
        if (oldBlockState != null) {
            oldSkylightAttenuation = oldBlockState.lightAttenuation;
        }

        int currentSkylight = blockPos.getSkyLight();
        int skylightAttenuation = targetBlockState.lightAttenuation;
        tmpQueue.clear();
        tmpQueue.addFirst(blockPos);
        BlockLightPropagator.propagateBlockDarkness(zone, tmpQueue);
        tmpQueue.clear();
        tmpQueue.addFirst(blockPos);
        BlockLightPropagator.propagateBlockLights(zone, tmpQueue);
        boolean propagateShade = currentSkylight > 0 && skylightAttenuation > oldSkylightAttenuation;
        boolean propagateSkylight = currentSkylight != 15 && skylightAttenuation < oldSkylightAttenuation;
        if (propagateShade || propagateSkylight) {
            tmpQueue.clear();
            if (propagateShade) {
                tmpQueue.addFirst(blockPos);
                BlockSetter.get().skylightProp.propagateShade(zone, tmpQueue);
            } else {
                tmpQueue.addFirst(blockPos.getOffsetBlockPos(zone, Direction.POS_X));
                tmpQueue.addFirst(blockPos.getOffsetBlockPos(zone, Direction.POS_Y));
                tmpQueue.addFirst(blockPos.getOffsetBlockPos(zone, Direction.POS_Z));
                tmpQueue.addFirst(blockPos.getOffsetBlockPos(zone, Direction.NEG_X));
                tmpQueue.addFirst(blockPos.getOffsetBlockPos(zone, Direction.NEG_Y));
                tmpQueue.addFirst(blockPos.getOffsetBlockPos(zone, Direction.NEG_Z));
                BlockSetter.get().skylightProp.propagateSkyLights(zone, tmpQueue);
            }
        }

    }

    public static void setBlockAt(Zone zone, BlockState state, BlockPosition pos) {
        if (IClientNetworkManager.isConnected()) {
            IClientNetworkManager.sendAsClient(new PlaceBlockPacket(zone, pos, state));
        }
        setBlockAt(zone, state, pos, BlockReplaceSettings.DEFAULT);
    }

    public static void setBlockAt(Zone zone, BlockState state, Vector3 vector3, BlockReplaceSettings settings) {
        setBlockAt(zone, state, (int) vector3.x, (int) vector3.y, (int) vector3.z, settings);
    }

    public static void setBlockAt(Zone zone, BlockState state, int x, int y, int z, BlockReplaceSettings settings) {
        setBlockAt(zone, state, getBlockPosAtVec(zone, x, y, z), settings);
    }

    public static void setBlockAt(Zone zone, BlockState state, Vector3 vector3) {
        setBlockAt(zone, state, (int) vector3.x, (int) vector3.y, (int) vector3.z);
    }

    public static void setBlockAt(Zone zone, BlockState state, int x, int y, int z) {
        setBlockAt(zone, state, getBlockPosAtVec(zone, x, y, z));
    }

    public static Chunk getChunkAtVec(Zone zone, int x, int y, int z) {
        return getChunkAtVec(zone, new Vector3(x, y, z));
    }

    public static Chunk getChunkAtVec(Zone zone, Vector3 vector3) {
        int cx = Math.floorDiv((int) vector3.x, 16);
        int cy = Math.floorDiv((int) vector3.y, 16);
        int cz = Math.floorDiv((int) vector3.z, 16);

        Chunk c = zone.getChunkAtChunkCoords(cx, cy, cz);
        if (c == null) {
            c = new Chunk(cx, cy, cz);
            c.initChunkData();
            zone.addChunk(c);
        }

        return c;
    }

    public static BlockPosition getBlockPosAtVec(Zone zone, int x, int y, int z) {
        int cx = Math.floorDiv(x, 16);
        int cy = Math.floorDiv(y, 16);
        int cz = Math.floorDiv(z, 16);

        Chunk c = zone.getChunkAtChunkCoords(cx, cy, cz);
        if (c == null) {
            c = new Chunk(cx, cy, cz);
            c.initChunkData();
            zone.zoneGenerator.generateForChunkColumn(zone, new ChunkColumn(c.chunkX, c.chunkY, c.chunkZ));
            zone.addChunk(c);
        }

        x -= 16 * cx;
        y -= 16 * cy;
        z -= 16 * cz;
        return new BlockPosition(c, x, y, z);
    }

    public static BlockPosition getBlockPosAtVec(Zone zone, Vector3 vector3) {
        return getBlockPosAtVec(zone, (int) vector3.x, (int) vector3.y, (int) vector3.z);
    }

    public static Color blockLightToColor(short packedColor) {
        int red = packedColor >> 8;
        int green = (packedColor - (red << 8)) >> 4;
        int blue = ((packedColor - (red << 8)) - (green << 4));
        return new Color(red, green, blue, 255);
    }

    public static Vector3 blockLightToVec3(short packedColor) {
        int red = packedColor >> 8;
        int green = (packedColor - (red << 8)) >> 4;
        int blue = ((packedColor - (red << 8)) - (green << 4));
        return new Vector3(red, green, blue);
    }

    public static Vector4 blockLightToVec4(short packedColor) {
        return new Vector4(blockLightToVec3(packedColor), 255);
    }

}
