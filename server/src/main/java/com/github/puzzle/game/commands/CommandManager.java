package com.github.puzzle.game.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

public class CommandManager {
    public static CommandDispatcher<CommandSource> consoledispatcher = new CommandDispatcher<>();

    public static CommandDispatcher<PuzzleCommandSource> dispatcher = new CommandDispatcher<>();

    public static LiteralArgumentBuilder<PuzzleCommandSource> literal(String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }

    public static <T> RequiredArgumentBuilder<PuzzleCommandSource, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

}
