package dev.crmodders.puzzle.bugfixes;

import finalforeach.cosmicreach.blocks.Block;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({Block.class})
public class BlockRegisterer {
    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("Block");
    @Shadow
    @Final
    public static Map<String, Block> blocksByName;
    @Shadow
    @Final
    public static Block AIR;

    public BlockRegisterer() {
    }

//    @Overwrite
//    public static Block getInstance(String blockName) {
//        Block block = blocksByName.get(blockName);
//        if (block == null && AIR != null) {
//            LOGGER.warn("null-block returned by Block.getInstance, please report", new Exception());
//        }
//
//        return block;
//    }
}
