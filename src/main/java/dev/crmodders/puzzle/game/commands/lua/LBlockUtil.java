package dev.crmodders.puzzle.game.commands.lua;

import com.badlogic.gdx.utils.Queue;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.BlockSetter;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;

public class LBlockUtil {

    public static Block getBlock(String id) {
        return Block.getInstance(id);
    }

    public static BlockState getBlockState(String id) {
        return BlockState.getInstance(id);
    }

    public static void setBlockState(Zone zone, BlockState block, int x, int y, int z) {
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
        BlockSetter.replaceBlock(zone, block, new BlockPosition(c, x, y, z), new Queue<>());
    }

}
