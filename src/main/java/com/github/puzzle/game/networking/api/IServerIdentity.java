package com.github.puzzle.game.networking.api;

import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;

public interface IServerIdentity {

    void setModdedState(String clientName, boolean isModded);
    boolean isModded();
    String getClientName();

    void setModList(Pair<String, String>[] modList);
    Pair<String, String>[] getModList();

}
