package com.github.puzzle.game.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector4;
import com.github.puzzle.core.Constants;
import com.github.puzzle.core.loader.meta.EnvType;
import com.github.puzzle.game.networking.api.IServerIdentity;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.networking.netty.packets.blocks.BlockReplacePacket;
import finalforeach.cosmicreach.networking.netty.packets.blocks.BreakBlockPacket;
import finalforeach.cosmicreach.networking.netty.packets.blocks.PlaceBlockPacket;
import finalforeach.cosmicreach.world.BlockSetter;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;

public class BlockUtil {

    public static void setBlockAt(Zone zone, BlockState state, BlockPosition vector3) {
        if (IClientNetworkManager.isConnected()) {
            IClientNetworkManager.sendAsClient(new BreakBlockPacket(zone, vector3, state));
            IClientNetworkManager.sendAsClient(new PlaceBlockPacket(zone, vector3, state));
        }
        BlockSetter.get().replaceBlock(zone, state, vector3);

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
