package com.github.puzzle.game.commands;

import com.github.puzzle.core.annotation.Internal;
import com.github.puzzle.core.loader.meta.Env;
import com.github.puzzle.core.loader.meta.EnvType;
import com.mojang.brigadier.CommandDispatcher;

@Env(EnvType.CLIENT)
public class ClientCommandManager {

    public static CommandDispatcher<ConsoleCommandSource> CLIENT_DISPATCHER = new CommandDispatcher<>();

    @Internal
    public static void registerCommands() {

    }

}
