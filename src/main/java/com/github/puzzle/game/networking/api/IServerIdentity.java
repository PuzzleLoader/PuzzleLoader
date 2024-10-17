package com.github.puzzle.game.networking.api;

public interface IServerIdentity {

    void setModdedState(String clientName, boolean name);
    boolean isModded();
    String getClientName();

}
