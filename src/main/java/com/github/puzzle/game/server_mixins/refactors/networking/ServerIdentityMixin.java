package com.github.puzzle.game.server_mixins.refactors.networking;

import com.github.puzzle.game.networking.api.IServerIdentity;
import finalforeach.cosmicreach.networking.server.ServerIdentity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerIdentity.class)
public class ServerIdentityMixin implements IServerIdentity {

    @Unique
    String puzzleLoader$name;
    @Unique
    boolean puzzleLoader$isModded;

    @Override
    public void setModdedState(String clientName, boolean modded) {
        puzzleLoader$name = clientName;
        puzzleLoader$isModded = modded;
    }

    @Override
    public boolean isModded() {
        return puzzleLoader$isModded;
    }

    @Override
    public String getClientName() {
        return puzzleLoader$name;
    }
}
