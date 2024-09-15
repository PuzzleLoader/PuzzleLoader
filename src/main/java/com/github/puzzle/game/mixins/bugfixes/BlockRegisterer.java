package com.github.puzzle.game.mixins.bugfixes;

import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.*;

import java.util.Map;

@Mixin(Block.class)
public class BlockRegisterer {

    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("Block");

    @Shadow @Final public static Block AIR;

    @Shadow @Final public static Block WATER;

    @Shadow @Final public static Block DIRT;

    @Shadow @Final public static Map<String, Block> blocksByStringId;


}