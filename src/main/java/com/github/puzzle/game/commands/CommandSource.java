package com.github.puzzle.game.commands;

import com.github.puzzle.core.loader.meta.Env;
import com.github.puzzle.core.loader.meta.EnvType;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.world.World;

public interface CommandSource {

    @Env(EnvType.SERVER)
    NetworkIdentity getIdentity();

    EnvType getSide();
    World getWorld();

}
