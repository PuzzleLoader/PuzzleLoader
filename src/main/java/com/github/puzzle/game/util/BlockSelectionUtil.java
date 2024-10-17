package com.github.puzzle.game.util;

import com.github.puzzle.core.loader.util.MethodUtil;
import com.github.puzzle.core.loader.util.Reflection;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.blocks.BlockStateMissing;

import java.lang.reflect.Method;

public class BlockSelectionUtil {

    public static BlockPosition getBlockPositionLookingAt() {
        try {
            Class<?> o = Class.forName("finalforeach.cosmicreach.BlockSelection");
            Method method = Reflection.getMethod(o, "getBlockPositionLookingAt");
            return (BlockPosition) MethodUtil.runStaticMethod(method);
        } catch (ClassNotFoundException e) {
            return new BlockPosition(null, 0, 0, 0);
        }
    }

    public static BlockState getBlockLookingAt() {
        try {
            Class<?> o = Class.forName("finalforeach.cosmicreach.BlockSelection");
            Method method = Reflection.getMethod(o, "getBlockLookingAt");
            return (BlockState) MethodUtil.runStaticMethod(method);
        } catch (ClassNotFoundException e) {
            return BlockStateMissing.fromMissingKey("nuhuh");
        }
    }

}
