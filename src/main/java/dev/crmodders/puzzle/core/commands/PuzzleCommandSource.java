package dev.crmodders.puzzle.core.commands;

import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.world.World;

public class PuzzleCommandSource implements CommandSource {

    final Account account;
    final Chat chat;
    final World world;
    final Player player;

    public PuzzleCommandSource(Account account, Chat chat, World world, Player player) {
        this.account = account;
        this.chat = chat;
        this.world = world;
        this.player = player;
    }

    @Override
    public Account getAccount() {
        return account;
    }

    @Override
    public Chat getChat() {
        return chat;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
