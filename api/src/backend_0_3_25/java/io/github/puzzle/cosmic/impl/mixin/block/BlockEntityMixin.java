package io.github.puzzle.cosmic.impl.mixin.block;

import finalforeach.cosmicreach.blockentities.BlockEntity;
import finalforeach.cosmicreach.util.Identifier;
import io.github.puzzle.cosmic.api.block.IPuzzleBlockEntity;
import io.github.puzzle.cosmic.api.block.IPuzzleBlockState;
import io.github.puzzle.cosmic.api.entity.player.IPuzzlePlayer;
import io.github.puzzle.cosmic.api.util.IPuzzleIdentifier;
import io.github.puzzle.cosmic.api.world.IPuzzleZone;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockEntity.class)
public class BlockEntityMixin implements IPuzzleBlockEntity {

    @Shadow int x;
    @Shadow int y;
    @Shadow int z;

    @Unique
    BlockEntity puzzleLoader$entity = IPuzzleBlockEntity.as(this);

    @Override
    public int _getGlobalX() {
        return puzzleLoader$entity.getGlobalX();
    }

    @Override
    public int _getGlobalY() {
        return puzzleLoader$entity.getGlobalY();
    }

    @Override
    public int _getGlobalZ() {
        return puzzleLoader$entity.getGlobalZ();
    }

    @Override
    public int _getLocalX() {
        int chunkX = Math.floorDiv(x, 16);
        return x - chunkX * 16;
    }

    @Override
    public int _getLocalY() {
        int chunkY = Math.floorDiv(y, 16);
        return y - chunkY * 16;
    }

    @Override
    public int _getLocalZ() {
        int chunkZ = Math.floorDiv(z, 16);
        return z - chunkZ * 16;
    }

    @Override
    public IPuzzleIdentifier _getIdentifier() {
        return (IPuzzleIdentifier) Identifier.of(puzzleLoader$entity.getBlockEntityId());
    }

    @Override
    public void _onCreate(IPuzzleBlockState iPuzzleBlockState) {
        puzzleLoader$entity.onCreate(iPuzzleBlockState.as());
    }

    @Override
    public void _onLoad() {
        puzzleLoader$entity.onLoad();
    }

    @Override
    public void _onUnload() {
        puzzleLoader$entity.onUnload();
    }

    @Override
    public void _setTicking(boolean b) {
        puzzleLoader$entity.setTicking(b);
    }

    @Override
    public void _onTick() {
        puzzleLoader$entity.onTick();
    }

    @Override
    public boolean _isTicking() {
        return puzzleLoader$entity.isTicking();
    }

    @Override
    public void _onInteract(IPuzzlePlayer iPuzzlePlayer, IPuzzleZone iPuzzleZone) {
        puzzleLoader$entity.onInteract(iPuzzlePlayer.as(), iPuzzleZone.as());
    }

    @Override
    public void _onSetBlockState(IPuzzleBlockState iPuzzleBlockState) {
        puzzleLoader$entity.onSetBlockState(iPuzzleBlockState.as());
    }

    @Override
    public void _setZone(IPuzzleZone iPuzzleZone) {
        puzzleLoader$entity.setZone(iPuzzleZone.as());
    }

    @Override
    public IPuzzleBlockState _getBlockState() {
        return IPuzzleBlockState.as(puzzleLoader$entity.getBlockState());
    }

    @Override
    public BlockEntity as() {
        return puzzleLoader$entity;
    }
}
