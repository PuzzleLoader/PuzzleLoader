package com.github.puzzle.game.commands;

import com.github.puzzle.core.loader.meta.EnvType;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.chat.IChat;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.world.World;

//Do not use puzzle specific things in this file, shared with paradox
public record ConsoleCommandSource(
        IChat chat,
        World world
) implements CommandSource {

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
    public EnvType getSide() {
        return EnvType.SERVER;
    }

    @Override
    public NetworkIdentity getIdentity() {
        return null;
    }

    @Override
    public Player getPlayer() {
        return null;
    }
}