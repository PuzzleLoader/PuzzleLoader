package com.github.puzzle.game.commands.command.console;

import com.github.puzzle.game.commands.CommandManager;
import com.github.puzzle.game.commands.ConsoleCommandSource;
import com.github.puzzle.game.util.CommandUtil;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import finalforeach.cosmicreach.networking.packets.MessagePacket;
import finalforeach.cosmicreach.networking.server.ServerSingletons;

import static com.github.puzzle.game.mod.ServerPuzzle.SERVER_ACCOUNT;

public class SayCommand implements Command<ConsoleCommandSource> {

    public static void register() {
        LiteralArgumentBuilder<ConsoleCommandSource> builder = CommandUtil.literal("say");
        builder.then(
                CommandUtil.argument("text", StringArgumentType.greedyString())
        ).executes(new SayCommand());

        CommandManager.CONSOLE_DISPATCHER.register(builder);
    }

    @Override
    public int run(CommandContext<ConsoleCommandSource> context) throws CommandSyntaxException {
        String message = StringArgumentType.getString(context, "txt");
        if(message.length() > MessagePacket.MAX_MESSAGE_LENGTH)
        {
            System.out.println("Message is grater than 256 chars");
            return 0;
        }
        var pack = new MessagePacket("[Server] "+ message);
        pack.playerUniqueId = SERVER_ACCOUNT.getUniqueId();
        ServerSingletons.SERVER.broadcastToAll(pack);
        return 0;
    }

}
