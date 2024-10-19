package com.github.puzzle.game.engine.rendering.text;

import com.badlogic.gdx.graphics.Color;

public class FormatColors {

    public static final String FORMAT_KEY = "§";

    public static final Color BLACK = Color.valueOf("#000000");
    public static final Color DARK_BLUE = Color.valueOf("#0000AA");
    public static final Color DARK_GREEN = Color.valueOf("#00AA00");
    public static final Color DARK_AQUA = Color.valueOf("#00AAAA");
    public static final Color DARK_RED = Color.valueOf("#AA0000");
    public static final Color DARK_PURPLE = Color.valueOf("#AA00AA");
    public static final Color GOLD = Color.valueOf("#FFAA00");
    public static final Color GRAY = Color.valueOf("#AAAAAA");
    public static final Color DARK_GRAY = Color.valueOf("#555555");
    public static final Color BLUE = Color.valueOf("#5555FF");
    public static final Color GREEN = Color.valueOf("#55FF55");
    public static final Color AQUA = Color.valueOf("#55FFFF");
    public static final Color RED = Color.valueOf("#FF5555");
    public static final Color LIGHT_PURPLE = Color.valueOf("#FF55FF");
    public static final Color YELLOW = Color.valueOf("#FFFF55");
    public static final Color WHITE = Color.valueOf("#FFFFFF");

    public static Color toColor(String c) {
        return toColor(c, Color.WHITE.cpy());
    }

    public static Color toColor(String c, Color resetColor){
        c = c.replaceFirst(FORMAT_KEY, "");

        return switch (c) {
            case "r" -> resetColor.cpy();
            case "0" -> BLACK.cpy();
            case "1" -> DARK_BLUE.cpy();
            case "2" -> DARK_GREEN.cpy();
            case "3" -> DARK_AQUA.cpy();
            case "4" -> DARK_RED.cpy();
            case "5" -> DARK_PURPLE.cpy();
            case "6" -> GOLD.cpy();
            case "7" -> GRAY.cpy();
            case "8" -> DARK_GRAY.cpy();
            case "9" -> BLUE.cpy();
            case "a" -> GREEN.cpy();
            case "b" -> AQUA.cpy();
            case "c" -> RED.cpy();
            case "d" -> LIGHT_PURPLE.cpy();
            case "e" -> YELLOW.cpy();
            case "f" -> WHITE.cpy();
            default -> {
                c = c.strip();
                if (c.startsWith("[") && c.endsWith("]")) {
                    c = c.replaceAll("\\[", "").replaceAll("]", "");
                    yield Color.valueOf("#" + c);
                } else {
                    yield null;
                }
            }
        };
    }

}
