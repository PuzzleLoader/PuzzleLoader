package com.github.puzzle.game.commands;

import com.github.puzzle.core.loader.meta.Env;
import com.github.puzzle.core.loader.meta.EnvType;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.networking.server.ServerSingletons;
import finalforeach.cosmicreach.world.World;

@Env(EnvType.SERVER)
public record ConsoleCommandSource(
        Chat chat,
        World world
) implements CommandSource {

    @Override
    public Account getAccount() {
        return null;
    }

    @Override
    public Chat getChat() {
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