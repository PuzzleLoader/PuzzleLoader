package io.github.puzzle.game.mixins.refactors.be;

import com.llamalad7.mixinextras.sugar.Local;
import finalforeach.cosmicreach.io.ChunkLoader;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;
import finalforeach.cosmicreach.worldgen.ChunkColumn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ChunkLoader.class)
public class ChunkLoaderMixin {
    @Inject(method = "readRegion", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/io/ChunkLoader;readChunk(ILfinalforeach/cosmicreach/io/ChunkByteReader;Lfinalforeach/cosmicreach/world/Zone;Lfinalforeach/cosmicreach/world/Chunk;)V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void shiftZoneAddChunk(Zone zone, int rx, int ry, int rz, ChunkColumn cc, CallbackInfo ci, @Local Chunk chunk) {
        zone.addChunk(chunk);
    }

    @Redirect(method = "readRegion", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/world/Zone;addChunk(Lfinalforeach/cosmicreach/world/Chunk;)V"))
    private static void removeOldAddChunk(Zone instance, Chunk chunk) {}
}