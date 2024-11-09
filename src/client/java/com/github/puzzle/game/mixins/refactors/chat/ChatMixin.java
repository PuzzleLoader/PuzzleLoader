package com.github.puzzle.game.mixins.refactors.chat;

import com.github.puzzle.game.commands.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.chat.ChatMessage;
import finalforeach.cosmicreach.chat.commands.Command;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.networking.client.ChatSender;
import finalforeach.cosmicreach.networking.client.ClientNetworkManager;
import finalforeach.cosmicreach.networking.packets.CommandPacket;
import finalforeach.cosmicreach.networking.packets.MessagePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ChatSender.class)
public abstract class ChatMixin {

    /**
     * @author Mr_Zombii
     * @reason Add Proper Command Support
     */
    @Overwrite
    public static void sendMessageOrCommand(Chat chat, Account account, String messageText) {
        CommandManager.initCommands();

        if (messageText == null || messageText.isBlank() || messageText.isEmpty()) return;

        final char firstChar = messageText.charAt(0);

        if (firstChar != '?' && firstChar != '/') {
            chat.addMessage(account, messageText);
            if (ClientNetworkManager.isConnected()) ClientNetworkManager.sendAsClient(new MessagePacket(messageText));
            return;
        }

        if (account == null) return;

        final String commandText = messageText.substring(1);
        final ChatMessage message = new ChatMessage(account, messageText, System.currentTimeMillis());

        chat.addToMessageQueue(message);

        switch (firstChar) {
            case '?':
                try {
                    ClientCommandManager.DISPATCHER.execute(commandText, new ClientCommandSource(GameSingletons.client().getLocalPlayer(), GameSingletons.world, chat));
                } catch (CommandSyntaxException e) {
                    Chat.MAIN_CLIENT_CHAT.addMessage(null, "Could not execute command " + messageText);
                }
                break;
            case '/':
                if (!GameSingletons.isHost)
                    ClientNetworkManager.sendAsClient(new CommandPacket(messageText.substring(1).split(" ")));
                else {
                    try {
                        CommandManager.DISPATCHER.execute(commandText, new SinglePlayerServerCommandSource(null, GameSingletons.world, chat));
                    } catch (CommandSyntaxException e) {
                        Chat.MAIN_CLIENT_CHAT.addMessage(null, "Could not execute command " + messageText);
                    }
                }

        }
        chat.pruneIfTooManyMessages();
    }
}
