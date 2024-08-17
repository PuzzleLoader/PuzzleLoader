package com.github.puzzle.game.items;

import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.items.ISlotContainer;
import finalforeach.cosmicreach.items.ItemSlot;

public interface IStorageContainer {
    default ItemSlot getInputSlots() { return null; }
    default ItemSlot getOutputSlots() { return null; }
    default ItemSlot getSlotsFromDirection(BlockPosition blockPosition) { return null; }
    default ISlotContainer getContainer() { return null; }
}
