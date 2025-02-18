package io.github.puzzle.cosmic.api.block;

import io.github.puzzle.cosmic.api.entity.player.IPuzzlePlayer;
import io.github.puzzle.cosmic.api.world.IPuzzleZone;
import io.github.puzzle.cosmic.api.util.IPuzzleIdentifier;
import io.github.puzzle.cosmic.util.ChangeType;

public interface IPuzzleBlockEntity {

    int getGlobalX();
    int getGlobalY();
    int getGlobalZ();

    int getLocalX();
    int getLocalY();
    int getLocalZ();

    IPuzzleIdentifier getIdentifier();

    void onCreate(IPuzzleBlockState state);

    void onLoad();
    void onUnload();

    void setTicking(boolean ticking);
    void onTick();

    boolean isTicking();

    void onInteract(IPuzzlePlayer player, IPuzzleZone zone);
    void onSetBlockState(IPuzzleBlockState state);
    void setZone(IPuzzleZone zone);

    IPuzzleBlockState getBlockState();

    @ChangeType("BlockEntity")
    Object as();

}
