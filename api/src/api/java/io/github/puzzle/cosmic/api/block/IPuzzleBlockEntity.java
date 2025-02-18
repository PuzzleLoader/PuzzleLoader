package io.github.puzzle.cosmic.api.block;

import io.github.puzzle.cosmic.api.entity.player.IPuzzlePlayer;
import io.github.puzzle.cosmic.api.world.IPuzzleZone;
import io.github.puzzle.cosmic.api.util.IPuzzleIdentifier;
import io.github.puzzle.cosmic.util.ApiDeclaration;

@ApiDeclaration(api = IPuzzleBlockEntity.class, impl = "BlockEntity")
public interface IPuzzleBlockEntity {

    int _getGlobalX();
    int _getGlobalY();
    int _getGlobalZ();

    int _getLocalX();
    int _getLocalY();
    int _getLocalZ();

    IPuzzleIdentifier _getIdentifier();

    void _onCreate(IPuzzleBlockState state);

    void _onLoad();
    void _onUnload();

    void _setTicking(boolean ticking);
    void _onTick();

    boolean _isTicking();

    void _onInteract(IPuzzlePlayer player, IPuzzleZone zone);
    void _onSetBlockState(IPuzzleBlockState state);
    void _setZone(IPuzzleZone zone);

    IPuzzleBlockState _getBlockState();

}
