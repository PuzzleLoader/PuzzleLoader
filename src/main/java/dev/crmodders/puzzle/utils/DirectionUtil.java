package dev.crmodders.puzzle.utils;

import finalforeach.cosmicreach.constants.Direction;

public class DirectionUtil {

    public static Direction opposite(Direction d) {
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