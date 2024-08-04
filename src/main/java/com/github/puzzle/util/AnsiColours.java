package com.github.puzzle.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum AnsiColours {
    RESET("\u001B[0m"),
    BLACK("\u001B[30m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[37m"),

    BRIGHT_BLACK("\u001B[90m"),
    BRIGHT_RED("\u001B[91m"),
    BRIGHT_GREEN("\u001B[92m"),
    BRIGHT_YELLOW("\u001B[93m"),
    BRIGHT_BLUE("\u001B[94m"),
    BRIGHT_PURPLE("\u001B[95m"),
    BRIGHT_CYAN("\u001B[96m"),
    BRIGHT_WHITE("\u001B[97m"),

    BG_BLACK("\u001B[40m"),
    BG_RED("\u001B[41m"),
    BG_GREEN("\u001B[42m"),
    BG_YELLOW("\u001B[43m"),
    BG_BLUE("\u001B[44m"),
    BG_PURPLE("\u001B[45m"),
    BG_CYAN("\u001B[46m"),
    BG_WHITE("\u001B[47m"),

    BRIGHT_BG_BLACK("\u001B[100m"),
    BRIGHT_BG_RED("\u001B[101m"),
    BRIGHT_BG_GREEN("\u001B[102m"),
    BRIGHT_BG_YELLOW("\u001B[103m"),
    BRIGHT_BG_BLUE("\u001B[104m"),
    BRIGHT_BG_PURPLE("\u001B[105m"),
    BRIGHT_BG_CYAN("\u001B[106m"),
    BRIGHT_BG_WHITE("\u001B[107m");

    final String code;

    AnsiColours(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public AnsiColours getBright() {
        return switch (this) {
            case BLACK -> BRIGHT_BLACK;
            case RED -> BRIGHT_RED;
            case GREEN -> BRIGHT_GREEN;
            case YELLOW -> BRIGHT_YELLOW;
            case BLUE -> BRIGHT_BLUE;
            case PURPLE -> BRIGHT_PURPLE;
            case CYAN -> BRIGHT_CYAN;
            case WHITE -> BRIGHT_WHITE;
            case BG_BLACK -> BRIGHT_BG_BLACK;
            case BG_RED -> BRIGHT_BG_RED;
            case BG_GREEN -> BRIGHT_BG_GREEN;
            case BG_YELLOW -> BRIGHT_BG_YELLOW;
            case BG_BLUE -> BRIGHT_BG_BLUE;
            case BG_PURPLE -> BRIGHT_BG_PURPLE;
            case BG_CYAN -> BRIGHT_BG_CYAN;
            case BG_WHITE -> BRIGHT_BG_WHITE;
            default -> this;
        };
    }
    
    public AnsiColours getBackground() {
        return switch (this) {
            case BLACK -> BG_BLACK;
            case RED -> BG_RED;
            case GREEN -> BG_GREEN;
            case YELLOW -> BG_YELLOW;
            case BLUE -> BG_BLUE;
            case PURPLE -> BG_PURPLE;
            case CYAN -> BG_CYAN;
            case WHITE -> BG_WHITE;
            case BRIGHT_BLACK -> BRIGHT_BG_BLACK;
            case BRIGHT_RED -> BRIGHT_BG_RED;
            case BRIGHT_GREEN -> BRIGHT_BG_GREEN;
            case BRIGHT_YELLOW -> BRIGHT_BG_YELLOW;
            case BRIGHT_BLUE -> BRIGHT_BG_BLUE;
            case BRIGHT_PURPLE -> BRIGHT_BG_PURPLE;
            case BRIGHT_CYAN -> BRIGHT_BG_CYAN;
            case BRIGHT_WHITE -> BRIGHT_BG_WHITE;
            default -> this;
        };
    }
    
    public String toString(){
        return code;
    }

    public boolean isBright() {
        return name().startsWith("BRIGHT_");
    }

    public boolean isBackground() {
        return name().contains("BG");
    }

    @Contract(pure = true)
    public static @NotNull String stripAnsiCodes(@NotNull String input) {
        return input.replaceAll("\u001B\\[[;\\d]*m", "");
    }

    public static @NotNull String apply(@NotNull Object obj, @NotNull AnsiColours colour) {
        return colour.getCode() + obj.toString() + RESET.getCode();
    }
}
