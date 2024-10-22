package com.github.puzzle.game.commands;

import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.world.World;

public record ServerCommandSource(
        NetworkIdentity identity,
        World world,
        Chat chat
) implements CommandSource {

    @Override
    public NetworkIdentity getIdentity() {
        return identity;
    }

    @Override
    public Player getPlayer() {
        return getIdentity().getPlayer();
    }

    @Override
    public Account getAccount() {
        return GameSingletons.getAccountFromPlayer(getIdentity().getPlayer());
    }

    @Override
    public Chat getChat() {
        return chat;
    }

    @Override
    public World getWorld() {
        return world;
    }
}
