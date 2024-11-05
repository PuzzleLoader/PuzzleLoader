package com.github.puzzle.game.commands;

import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.chat.IChat;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.world.World;
//Do not use puzzle specific things in this file, shared with paradox
public class PuzzleConsoleCommandSource implements CommandSource {

    final World world;
    final IChat chat;


    public PuzzleConsoleCommandSource(IChat chat, World world) {
        this.world = world;
        this.chat = chat;
    }

    @Override
    public Account getAccount() {
        return null;
    }

    @Override
    public IChat getChat() {
        return chat;
    }

    @Override
    public World getWorld() {
        return null;
    }

    @Override
    public Player getPlayer() {
        return null;
    }
}
