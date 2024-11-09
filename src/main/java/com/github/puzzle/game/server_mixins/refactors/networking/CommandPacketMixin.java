package com.github.puzzle.game.server_mixins.refactors.networking;

import com.github.puzzle.game.commands.CommandManager;
import com.github.puzzle.game.commands.ServerCommandSource;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.chat.commands.Command;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.networking.packets.CommandPacket;
import finalforeach.cosmicreach.networking.server.ServerIdentity;
import finalforeach.cosmicreach.networking.server.ServerSingletons;
import io.netty.channel.ChannelHandlerContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CommandPacket.class)
public class CommandPacketMixin {

    @Shadow
    String[] commandArgs;

    /**
     * @author Mr_Zombii
     * @reason Add Server-Side commands with brigadier
     */
    @Overwrite
    public void handle(final NetworkIdentity identity, ChannelHandlerContext ctx) {
        CommandManager.initCommands();

        StringBuilder commandText = new StringBuilder();
        for (String s : this.commandArgs) commandText.append(" ").append(s);
        commandText.deleteCharAt(0);

        if (!identity.isClient()) {
            Account acc = identity.getAccount();
            try {
                CommandManager.DISPATCHER.execute(commandText.toString(), new ServerCommandSource(acc.isOperator(), identity, GameSingletons.world, (account, messageText) -> {
                    ServerSingletons.SERVER.systemChat.addMessage(account, messageText);
                    ((ServerIdentity)identity).sendChatMessage(messageText);
                }));
            } catch (CommandSyntaxException e) {
                ((ServerIdentity)identity).sendChatMessage("Could not execute command server-side -> " + commandText);
                System.out.println("Player \"" + acc.getUsername() + "\" failed to execute command -> \"" + commandText + "\"");
                e.printStackTrace();
            }
        }
    }

}
