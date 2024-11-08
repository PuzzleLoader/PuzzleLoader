package com.github.puzzle.game.commands;

import com.badlogic.gdx.utils.Null;
import finalforeach.cosmicreach.ClientSingletons;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.chat.IChat;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.world.World;

public class SinglePlayerServerCommandSource extends ServerCommandSource {

    final Player player;

    public SinglePlayerServerCommandSource(
            Player player,
            World world,
            IChat chat
    ) {
        super(true, null, world, chat);

        this.player = player;
    }

    @Override @Null
    public NetworkIdentity getIdentity() {
        return null;
    }

    @Override
    public Account getAccount() {
        return ClientSingletons.ACCOUNT;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
