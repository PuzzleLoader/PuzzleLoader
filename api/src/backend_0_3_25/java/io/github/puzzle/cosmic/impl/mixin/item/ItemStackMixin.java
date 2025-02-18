package io.github.puzzle.cosmic.impl.mixin.item;

import finalforeach.cosmicreach.items.ItemStack;
import io.github.puzzle.cosmic.api.block.IPuzzleBlockPosition;
import io.github.puzzle.cosmic.api.block.IPuzzleBlockState;
import io.github.puzzle.cosmic.api.entity.player.IPuzzlePlayer;
import io.github.puzzle.cosmic.api.item.IPuzzleItem;
import io.github.puzzle.cosmic.api.item.IPuzzleItemSlot;
import io.github.puzzle.cosmic.api.item.IPuzzleItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemStack.class)
public class ItemStackMixin implements IPuzzleItemStack {

    @Unique
    ItemStack puzzleLoader$stack = IPuzzleItemStack.as(this);

    @Override
    public IPuzzleItemStack _copy() {
        return IPuzzleItemStack.as(puzzleLoader$stack.copy());
    }

    @Override
    public IPuzzleItem _getItem() {
        return IPuzzleItem.as(puzzleLoader$stack.getItem());
    }

    @Override
    public void _setItem(IPuzzleItem iPuzzleItem) {
        puzzleLoader$stack.setItem(iPuzzleItem.as());
    }

    @Override
    public void _cycleSwapGroupItem() {
        puzzleLoader$stack.cycleSwapGroupItem();
    }

    @Override
    public void _spawnItemEntityAt(IPuzzleBlockPosition iPuzzleBlockPosition) {
        puzzleLoader$stack.spawnItemEntityAt(iPuzzleBlockPosition.as());
    }

    @Override
    public boolean _useItem(IPuzzleItemSlot iPuzzleItemSlot, IPuzzlePlayer iPuzzlePlayer, IPuzzleBlockPosition iPuzzleBlockPosition) {
        return puzzleLoader$stack.useItem(iPuzzleItemSlot.as(), iPuzzlePlayer.as(), iPuzzleBlockPosition.as());
    }

    @Override
    public int _getDurability() {
        return puzzleLoader$stack.getDurability();
    }

    @Override
    public int _getMaxDurability() {
        return puzzleLoader$stack.getMaxDurability();
    }

    @Override
    public boolean _hasDurability() {
        return puzzleLoader$stack.hasDurability();
    }

    @Override
    public void _damage(int i) {
        puzzleLoader$stack.damage(i);
    }

    @Override
    public boolean _isBroken() {
        return puzzleLoader$stack.isBroken();
    }

    @Override
    public boolean _canTargetBlockForBreaking(IPuzzleBlockState iPuzzleBlockState) {
        return puzzleLoader$stack.canTargetBlockForBreaking(iPuzzleBlockState.as());
    }

    @Override
    public boolean _isEffectiveBreaking(IPuzzleBlockState iPuzzleBlockState) {
        return puzzleLoader$stack.isEffectiveBreaking(iPuzzleBlockState.as());
    }

    @Override
    public float _getEffectiveBreakingSpeed() {
        return puzzleLoader$stack.getEffectiveBreakingSpeed();
    }

    @Override
    public IPuzzleItemStack _setAmount(int i) {
        return IPuzzleItemStack.as(puzzleLoader$stack.setAmount(i));
    }

    @Override
    public String _getName() {
        return puzzleLoader$stack.getName();
    }
}
