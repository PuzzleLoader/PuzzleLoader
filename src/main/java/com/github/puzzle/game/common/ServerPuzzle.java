package com.github.puzzle.game.common;

import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.ModInitializer;
import com.github.puzzle.game.commands.CommandManager;
import com.github.puzzle.game.commands.ConsoleCommandSource;
import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.puzzle.BlockWrench;
import com.github.puzzle.game.items.puzzle.BuilderWand;
import com.github.puzzle.game.items.puzzle.CheckBoard;
import com.github.puzzle.game.items.puzzle.NullStick;

public class ServerPuzzle implements ModInitializer {

    public static IModItem DebugStick;
    public static IModItem CheckerBoard;
    public static IModItem BlockWrench;

    public ServerPuzzle() {
//        PuzzleRegistries.EVENT_BUS.subscribe(this);
    }

    @Override
    public void onInit() {
        DebugStick = IModItem.registerItem(new NullStick());
        CheckerBoard = IModItem.registerItem(new CheckBoard());
        BlockWrench = IModItem.registerItem(new BlockWrench());
        IModItem.registerItem(new BuilderWand());

        CommandManager.CONSOLE_DISPATCHER.register(CommandManager.createHelp(ConsoleCommandSource.class, "?", '/', CommandManager.CONSOLE_DISPATCHER));
        CommandManager.CONSOLE_DISPATCHER.register(CommandManager.createHelp(ConsoleCommandSource.class, "help", '/', CommandManager.CONSOLE_DISPATCHER));
    }
}
