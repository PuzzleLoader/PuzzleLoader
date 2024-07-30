package dev.crmodders.puzzle.game.mixins.refactors.chat;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.crmodders.puzzle.core.commands.CommandManager;
import dev.crmodders.puzzle.core.commands.PuzzleCommandSource;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.chat.ChatMessage;
import finalforeach.cosmicreach.chat.commands.Command;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Chat.class)
public abstract class ChatMixin {

    @Shadow public Queue<ChatMessage> messageQueue;

    @Shadow private Array<ChatMessage> activeMessages;

    @Shadow public int maxMessages;

    @Shadow public abstract void sendMessage(World world, Player player, Account account, String messageText);

    /**
     * @author Mr_Zombii
     * @reason Add Proper Command Support
     */
    @Overwrite
    public void sendMessageOrCommand(World world, Player player, Account account, String messageText) {
        ChatMessage message = new ChatMessage(account, messageText, System.currentTimeMillis());
        String commandChar = "/";

        // Force Command.java from Cosmic Reach to init the <clinit> block
        Command.registerCommand("asodfjoasdiofasdf", () -> new Command() {
            @Override
            public String getDescription() {
                return "";
            }
        });
        this.messageQueue.addFirst(message);
        if (messageText.startsWith(commandChar) && account != null) {
            try {
                CommandManager.dispatcher.execute(messageText.substring(1), new PuzzleCommandSource(account, (Chat) (Object) this, world, player));
            } catch (CommandSyntaxException e) {
                this.sendMessage(world, player, null, e.getMessage());
                System.out.println(messageText.substring(1));
                e.printStackTrace();
            }
        } else {
            this.activeMessages.insert(0, message);
        }

        while(this.activeMessages.size > this.maxMessages) {
            this.activeMessages.pop();
        }

        while(this.messageQueue.size > this.maxMessages) {
            this.messageQueue.removeLast();
        }

    }
}
