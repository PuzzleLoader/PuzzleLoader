package com.github.puzzle.core.terminal;

import com.github.puzzle.core.loader.util.AnsiColours;
import com.github.puzzle.game.ServerGlobals;
import com.github.puzzle.game.commands.CommandManager;
import com.github.puzzle.game.commands.ConsoleCommandSource;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import finalforeach.cosmicreach.networking.netty.NettyServer;
import finalforeach.cosmicreach.networking.server.ServerSingletons;
import net.minecrell.terminalconsole.SimpleTerminalConsole;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;

import static finalforeach.cosmicreach.GameSingletons.world;

public class PPLTerminalConsole extends SimpleTerminalConsole {

    NettyServer server;

    public PPLTerminalConsole(NettyServer server){
        this.server = server;
    }

    @Override
    protected LineReader buildReader(LineReaderBuilder builder){
        return super.buildReader(builder.appName("Puzzle Loader"));
    }

    @Override
    protected boolean isRunning() {
        return ServerGlobals.isRunning;
    }

    @Override
    protected void runCommand(String command) {
        try {
            ParseResults<ConsoleCommandSource> results = CommandManager.CONSOLE_DISPATCHER.parse(command,new ConsoleCommandSource(ServerSingletons.SERVER.systemChat, world));
            CommandSyntaxException e;
            if(results.getReader().canRead()) {
                if(results.getExceptions().size() == 1)
                    e = results.getExceptions().values().iterator().next();
                else
                    e = results.getContext().getRange().isEmpty() ? CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(results.getReader()) : CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(results.getReader());
                throw e;
            }
            CommandManager.CONSOLE_DISPATCHER.execute(new StringReader(command), new ConsoleCommandSource(ServerSingletons.SERVER.systemChat, world));
        } catch (CommandSyntaxException e) {
            System.out.print(e.getRawMessage().getString() + ": "+ AnsiColours.RED + command + AnsiColours.RESET + "\n");
//            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.print(e.getMessage() + "\n");
//            e.printStackTrace();
        }
    }

    @Override
    protected void shutdown() {

    }
}
