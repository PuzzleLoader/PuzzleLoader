package com.github.puzzle.game.commands;

import com.github.puzzle.core.annotation.Internal;
import com.github.puzzle.game.commands.command.console.SayCommand;
import com.mojang.brigadier.CommandDispatcher;

public class CommandManager {

    public static CommandDispatcher<ServerCommandSource> SERVER_DISPATCHER = new CommandDispatcher<>();
    public static CommandDispatcher<ConsoleCommandSource> CONSOLE_DISPATCHER = new CommandDispatcher<>();

    @Internal
    public static void registerCommands() {
        SayCommand.register();
    }

}
