package com.github.puzzle.game.networking.api;

import com.github.puzzle.core.loader.meta.Version;

import java.util.Map;

public interface IServerIdentity {

    void setModdedState(String clientName, boolean isModded);
    boolean isModded();
    String getClientName();

    void setModList(Map<String, Version> modList);
    Map<String, Version> getModList();

}
