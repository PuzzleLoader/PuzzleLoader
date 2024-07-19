package dev.crmodders.puzzle.game.mixins.refactors.saving;

import dev.crmodders.puzzle.game.serialization.api.IPuzzleBinarySerializer;
import finalforeach.cosmicreach.io.CosmicReachBinarySerializer;
import finalforeach.cosmicreach.world.EntityRegion;
import finalforeach.cosmicreach.world.Zone;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static finalforeach.cosmicreach.world.EntityRegion.getEntityRegionFolderName;

@Mixin(EntityRegion.class)
public class EntityRegionMixin {
    /**
     * @author Mr_Zombii
     * @reason Add NBT
     */
    @Overwrite
    public static String getEntityRegionFileName(Zone zone, int regionX, int regionY, int regionZ) {
        String regionFolderName = getEntityRegionFolderName(zone);
        CosmicReachBinarySerializer serializer = new CosmicReachBinarySerializer();
        return regionFolderName + "/entityRegion_" + regionX + "_" + regionY + "_" + regionZ + ((IPuzzleBinarySerializer) serializer).getFileExt();
    }
}
