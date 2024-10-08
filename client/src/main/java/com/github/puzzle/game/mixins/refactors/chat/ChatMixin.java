package com.github.puzzle.game.mixins.refactors.chat;

import com.github.puzzle.game.commands.CommandManager;
import com.github.puzzle.game.commands.PuzzleCommandSource;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.chat.ChatMessage;
import finalforeach.cosmicreach.chat.commands.Command;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.networking.client.ChatSender;
import finalforeach.cosmicreach.networking.client.ClientNetworkManager;
import finalforeach.cosmicreach.networking.netty.packets.MessagePacket;
import finalforeach.cosmicreach.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ChatSender.class)
public abstract class ChatMixin {

    /**
     * @author Mr_Zombii
     * @reason Add Proper Command Support
     */
    @Overwrite
    public static void sendMessageOrCommand(Chat chat, World world, Player player, Account account, String messageText) {
        if (messageText != null && !messageText.isEmpty()) {
            if (messageText.charAt(0) == '/' && account != null) {
                // Force Command.java load the <clinit> block
                Command.registerCommand(() -> new Command() {
                    @Override
                    public String getShortDescription() {
                        return "";
                    }
                }, "asodfjoasdiofasdf");

                ChatMessage message = new ChatMessage(account, messageText, System.currentTimeMillis());
                chat.addToMessageQueue(message);
                try {
                    CommandManager.dispatcher.execute(messageText.substring(1), new PuzzleCommandSource(account, chat, world, player));
                } catch (CommandSyntaxException e) {
                    chat.addMessage(world, player, null, messageText);
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    chat.addMessage(world, player, null, messageText);
                    e.printStackTrace();
                }
            } else {
                chat.addMessage(world, player, account, messageText);
                if (ClientNetworkManager.isConnected()) {
                    ClientNetworkManager.sendAsClient(new MessagePacket(messageText));
                }
            }

            chat.pruneIfTooManyMessages();
        }
    }
}
