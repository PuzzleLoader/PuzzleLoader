package com.github.puzzle.game.commands;

import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.chat.IChat;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.rendering.entities.EntityModelInstance;
import finalforeach.cosmicreach.world.World;

import javax.annotation.Nullable;

/**
 * @see CommandSource
 */
public record ClientCommandSource(
        Player player,
        World world,
        IChat chat
) implements CommandSource {

    @Override @Nullable
    public NetworkIdentity getIdentity() {
        return null;
    }

    @Override
    public Player getPlayer() {
        return getIdentity().getPlayer();
    }

    @Override
    public Account getAccount() {
        return player.getAccount();
    }

    @Override
    public IChat getChat() {
        return chat;
    }

    @Override
    public World getWorld() {
        return world;
    }

}
