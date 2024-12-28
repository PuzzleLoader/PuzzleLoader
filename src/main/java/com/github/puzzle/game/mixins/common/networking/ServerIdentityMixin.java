package com.github.puzzle.game.mixins.common.networking;

import com.github.puzzle.core.loader.meta.Version;
import com.github.puzzle.game.networking.api.IServerIdentity;
import finalforeach.cosmicreach.networking.server.ServerIdentity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin(ServerIdentity.class)
public class ServerIdentityMixin implements IServerIdentity {

    @Unique
    String puzzleLoader$name;
    @Unique
    boolean puzzleLoader$isModded;

    Map<String, Version> modList = new HashMap<>();

    @Override
    public void setModdedState(String clientName, boolean isModded) {
        puzzleLoader$name = clientName;
        puzzleLoader$isModded = isModded;
    }

    public void setModList(Map<String, Version> modList) {
        this.modList = modList;
    }

    public Map<String, Version> getModList() {
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
