package com.github.puzzle.game.util;

import com.github.puzzle.core.loader.util.MethodUtil;
import com.github.puzzle.core.loader.util.Reflection;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.blocks.BlockStateMissing;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BlockSelectionUtil {

    public static BlockPosition getBlockPositionLookingAt() {
        try {
            Class<?> gameState = Class.forName("finalforeach.cosmicreach.gamestates.GameState");
            Field inGameField = gameState.getDeclaredField("IN_GAME");
            Object inGame = inGameField.get(null);
            Object blockSelection = Reflection.getField(inGame, "blockSelection").get(inGame);
            Method method = Reflection.getMethod(blockSelection.getClass(), "getBlockPositionLookingAt");
            return (BlockPosition) MethodUtil.runMethod(blockSelection, method);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            return new BlockPosition(null, 0, 0, 0);
        }
    }

    public static BlockState getBlockLookingAt() {
        try {
            Class<?> gameState = Class.forName("finalforeach.cosmicreach.gamestates.GameState");
            Field inGameField = gameState.getDeclaredField("IN_GAME");
            Object inGame = inGameField.get(null);
            Object blockSelection = Reflection.getField(inGame, "blockSelection").get(inGame);
            Method method = Reflection.getMethod(blockSelection.getClass(), "getBlockLookingAt");
            return (BlockState) MethodUtil.runMethod(blockSelection, method);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            return BlockStateMissing.fromMissingKey("nuhuh");
        }
    }

}
