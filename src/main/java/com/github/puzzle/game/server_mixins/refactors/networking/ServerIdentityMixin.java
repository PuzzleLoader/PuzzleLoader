package com.github.puzzle.game.server_mixins.refactors.networking;

import com.github.puzzle.game.networking.api.IServerIdentity;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import finalforeach.cosmicreach.networking.server.ServerIdentity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerIdentity.class)
public class ServerIdentityMixin implements IServerIdentity {

    @Unique
    String puzzleLoader$name;
    @Unique
    boolean puzzleLoader$isModded;

    Pair<String, String>[] modList = new Pair[0];

    @Override
    public void setModdedState(String clientName, boolean modded) {
        puzzleLoader$name = clientName;
        puzzleLoader$isModded = modded;
    }

    public void setModList(Pair<String, String>[] modList) {
        this.modList = modList;
    }

    public Pair<String, String>[] getModList() {
        return modList;
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
