package com.github.puzzle.game.util;

import finalforeach.cosmicreach.blockevents.actions.BlockActionPlaySound2D;
import finalforeach.cosmicreach.blockevents.actions.BlockActionReplaceBlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockEventActionFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger("Puzzle | BlockActionFactory");

    public static BlockActionReplaceBlockState createReplaceBlockEvent(String blockStateId, int xOff, int yOff, int zOff) {
        BlockActionReplaceBlockState replace = new BlockActionReplaceBlockState();
        try {
            Reflection.setFieldContents(replace, "blockStateId", blockStateId);
            Reflection.setFieldContents(replace, "xOff", xOff);
            Reflection.setFieldContents(replace, "yOff", yOff);
            Reflection.setFieldContents(replace, "zOff", zOff);
        } catch (RuntimeException e) {
            LOGGER.error("createReplaceBlockEvent failed", e);
        }
        return replace;
    }

    public static BlockActionPlaySound2D createPlaySound2D(String sound, float volume, float pitch, float pan) {
        BlockActionPlaySound2D sound2D = new BlockActionPlaySound2D();
        try {
            Reflection.setFieldContents(sound2D, "sound", sound);
            Reflection.setFieldContents(sound2D, "volume", volume);
            Reflection.setFieldContents(sound2D, "pitch", pitch);
            Reflection.setFieldContents(sound2D, "pan", pan);
        } catch (RuntimeException e) {
            LOGGER.error("createPlaySound2D failed", e);
        }
        return sound2D;
    }

}
