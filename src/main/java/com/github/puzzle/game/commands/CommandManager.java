package com.github.puzzle.game.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

//Do not use puzzle specific things in this file, shared with paradox
public class CommandManager {
    public static CommandDispatcher<ConsoleCommandSource> CONSOLE_DISPATCHER = new CommandDispatcher<>();
    public static CommandDispatcher<ServerCommandSource> DISPATCHER = new CommandDispatcher<>();

    public static LiteralArgumentBuilder<ServerCommandSource> literal(String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }

    public static <T> RequiredArgumentBuilder<ServerCommandSource, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

}
