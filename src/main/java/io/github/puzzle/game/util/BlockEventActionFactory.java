package io.github.puzzle.game.util;

import finalforeach.cosmicreach.blockevents.actions.BlockActionPlaySound2D;
import finalforeach.cosmicreach.blockevents.actions.BlockActionReplaceBlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockEventActionFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger("Puzzle | BlockActionFactory");

    public static BlockActionReplaceBlockState createReplaceBlockEvent(String blockStateId, int xOff, int yOff, int zOff) {
        BlockActionReplaceBlockState replace = new BlockActionReplaceBlockState();
        try {
            Reflection.setField(replace, "blockStateId", blockStateId);
            Reflection.setField(replace, "xOff", xOff);
            Reflection.setField(replace, "yOff", yOff);
            Reflection.setField(replace, "zOff", zOff);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.error("createReplaceBlockEvent failed", e);
        }
        return replace;
    }

    public static BlockActionPlaySound2D createPlaySound2D(String sound, float volume, float pitch, float pan) {
        BlockActionPlaySound2D sound2D = new BlockActionPlaySound2D();
        try {
            Reflection.setField(sound2D, "sound", sound);
            Reflection.setField(sound2D, "volume", volume);
            Reflection.setField(sound2D, "pitch", pitch);
            Reflection.setField(sound2D, "pan", pan);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.error("createPlaySound2D failed", e);
        }
        return sound2D;
    }

}
