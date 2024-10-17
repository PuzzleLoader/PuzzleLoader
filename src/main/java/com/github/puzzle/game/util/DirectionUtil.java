package com.github.puzzle.game.util;

import finalforeach.cosmicreach.constants.Direction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class DirectionUtil {
    @Contract(pure = true)
    public static Direction opposite(@NotNull Direction d) {
        return switch (d) {
            case NEG_X -> Direction.POS_X;
            case POS_X -> Direction.NEG_X;
            case NEG_Y -> Direction.POS_Y;
            case POS_Y -> Direction.NEG_Y;
            case NEG_Z -> Direction.POS_Z;
            case POS_Z -> Direction.NEG_Z;
        };
    }
}