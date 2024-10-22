package com.github.puzzle.game.commands;

import com.github.puzzle.core.Constants;
import com.github.puzzle.core.loader.meta.Env;
import com.github.puzzle.core.loader.meta.EnvType;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.world.World;

public interface CommandSource {

    @Env(EnvType.SERVER)
    NetworkIdentity getIdentity();

    Player getPlayer();
    Account getAccount();
    Chat getChat();
    World getWorld();

    default EnvType getSide() {
        return Constants.SIDE;
    }

}
