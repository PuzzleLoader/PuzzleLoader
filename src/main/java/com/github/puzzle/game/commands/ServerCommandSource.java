package com.github.puzzle.game.commands;

import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.chat.IChat;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.world.World;

/**
 * @see CommandSource
 */
public class ServerCommandSource implements CommandSource {

    final NetworkIdentity identity;
    final World world;
    final IChat chat;
    final boolean hasOperator;

    public ServerCommandSource(
            boolean hasOperator,
            NetworkIdentity identity,
            World world,
            IChat chat
    ) {
        this.identity = identity;
        this.world = world;
        this.chat = chat;
        this.hasOperator = hasOperator;
    }

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
    public IChat getChat() {
        return chat;
    }

    @Override
    public World getWorld() {
        return world;
    }

    public boolean hasOperator() {
        return hasOperator;
    }
}
