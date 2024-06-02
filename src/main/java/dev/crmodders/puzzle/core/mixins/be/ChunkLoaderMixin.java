package dev.crmodders.puzzle.core.mixins.be;

import finalforeach.cosmicreach.io.ChunkByteReader;
import finalforeach.cosmicreach.io.ChunkLoader;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Region;
import finalforeach.cosmicreach.world.Zone;
import finalforeach.cosmicreach.worldgen.ChunkColumn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;
import java.io.RandomAccessFile;

@Mixin(ChunkLoader.class)
public class ChunkLoaderMixin {


    @Inject(method = "readRegion", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/io/ChunkLoader;readChunk(ILfinalforeach/cosmicreach/io/ChunkByteReader;Lfinalforeach/cosmicreach/world/Chunk;)V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void shiftZoneAddChunk(Zone zone, int rx, int ry, int rz, ChunkColumn cc, CallbackInfo ci, String regionFileName, File regionFile, Region region, RandomAccessFile raf, ChunkByteReader reader, int magic, int fileVersion, int compressionType, int numChunksWritten, Region var14, boolean isGenerated, int j, int chunkIndex, int byteOff, int byteSize, int chunkFileVersion, int cx, int cy, int cz, Chunk chunk) {
        zone.addChunk(chunk);
    }

    @Redirect(method = "readRegion", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/world/Zone;addChunk(Lfinalforeach/cosmicreach/world/Chunk;)V"))
    private static void removeOldAddChunk(Zone instance, Chunk chunk) {

    }

}