package com.github.puzzle.game.util;

import com.github.puzzle.game.commands.CommandSource;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

public class CommandUtil {

    public static <T extends CommandSource> LiteralArgumentBuilder<T> literal(String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }

    public static <A extends CommandSource, B> RequiredArgumentBuilder<A, B> argument(String name, ArgumentType<B> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

}
