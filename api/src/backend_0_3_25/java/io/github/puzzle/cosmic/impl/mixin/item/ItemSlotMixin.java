package io.github.puzzle.cosmic.impl.mixin.item;

import finalforeach.cosmicreach.items.ItemSlot;
import io.github.puzzle.cosmic.api.item.IPuzzleItem;
import io.github.puzzle.cosmic.api.item.IPuzzleItemSlot;
import io.github.puzzle.cosmic.api.item.IPuzzleItemStack;
import io.github.puzzle.cosmic.api.item.container.IPuzzleSlotContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemSlot.class)
public class ItemSlotMixin implements IPuzzleItemSlot {

    @Unique
    ItemSlot puzzleLoader$slot = IPuzzleItemSlot.as(this);

    @Override
    public void _set(IPuzzleItemSlot iPuzzleItemSlot) {
        puzzleLoader$slot.set(iPuzzleItemSlot.as());
    }

    @Override
    public void _setItemStack(IPuzzleItemStack iPuzzleItemStack) {
        puzzleLoader$slot.setItemStack(iPuzzleItemStack.as());
    }

    @Override
    public boolean _mergeFrom(IPuzzleItemSlot iPuzzleItemSlot) {
        return puzzleLoader$slot.mergeFrom(iPuzzleItemSlot.as());
    }

    @Override
    public boolean _merge(IPuzzleItemStack iPuzzleItemStack) {
        return puzzleLoader$slot.merge(iPuzzleItemStack.as());
    }

    @Override
    public boolean _mergeInto(IPuzzleItemSlot iPuzzleItemSlot) {
        return puzzleLoader$slot.mergeInto(iPuzzleItemSlot.as());
    }

    @Override
    public int _getSlotId() {
        return puzzleLoader$slot.getSlotId();
    }

    @Override
    public IPuzzleSlotContainer _getContainer() {
        return IPuzzleSlotContainer.as(puzzleLoader$slot.getContainer());
    }

    @Override
    public void _setContainer(IPuzzleSlotContainer iPuzzleSlotContainer) {
        puzzleLoader$slot.setContainer(iPuzzleSlotContainer.as());
    }

    @Override
    public void _addAmount(int i) {
        puzzleLoader$slot.addAmount(i);
    }

    @Override
    public void _onItemSlotUpdate() {
        puzzleLoader$slot.onItemSlotUpdate();
    }

    @Override
    public void _setOutputOnly(boolean b) {
        puzzleLoader$slot.setOutputOnly(b);
    }

    @Override
    public boolean _isOutputOnly() {
        return puzzleLoader$slot.isOutputOnly();
    }

    @Override
    public boolean _isEmpty() {
        return puzzleLoader$slot.isEmpty();
    }

    @Override
    public boolean _hasRoomFor(IPuzzleItem iPuzzleItem) {
        return puzzleLoader$slot.hasRoomFor(iPuzzleItem.as());
    }

    @Override
    public boolean _hasRoomFor(IPuzzleItemStack iPuzzleItemStack) {
        return puzzleLoader$slot.hasRoomFor(iPuzzleItemStack.as());
    }

    @Override
    public boolean _hasRoomFor(IPuzzleItem iPuzzleItem, int i) {
        return puzzleLoader$slot.hasRoomFor(iPuzzleItem.as());
    }

    @Override
    public boolean _addItemStack(IPuzzleItem iPuzzleItem, int i) {
        return puzzleLoader$slot.addItemStack(iPuzzleItem.as(), i);
    }

    @Override
    public boolean _addItemStack(IPuzzleItemStack iPuzzleItemStack) {
        return puzzleLoader$slot.addItemStack(iPuzzleItemStack.as());
    }
}
