package io.github.puzzle.game.mixins.bugfixes;

import finalforeach.cosmicreach.blocks.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.*;

import java.util.Map;

@Mixin(Block.class)
public class BlockRegisterer {

    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("Block");

    @Shadow @Final public static Map<String, Block> blocksByName;

    @Shadow @Final public static Block AIR;

    @Shadow @Final public static Block WATER;

    @Shadow @Final public static Block DIRT;

    /**
     * @author nanobass
     * @reason is replaced by flux, allows for much more advanced features
     *          than data modding
     */
    @Overwrite
    public static Block getInstance(String blockName) {
        Block block = blocksByName.get(blockName);
        if(block == null && AIR != null && WATER != null && DIRT != null) {
            LOGGER.warn("null-block returned by Block.getInstance, please report \"{}\"", blockName, new Exception());
        }
        return block;
    }

}