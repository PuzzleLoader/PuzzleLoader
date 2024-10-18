package com.github.puzzle.game.commands;

import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.world.World;
//Do not use puzzle specific things in this file, shared with paradox
public interface CommandSource {

    Account getAccount();
    Chat getChat();
    World getWorld();
    Player getPlayer();

}
