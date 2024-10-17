package com.github.puzzle.game.items;

import finalforeach.cosmicreach.constants.Direction;
import finalforeach.cosmicreach.items.ISlotContainer;
import finalforeach.cosmicreach.items.ItemSlot;

import java.util.List;

public interface IStorageContainer {
    default List<ItemSlot> getInputSlots() { return null; }
    default List<ItemSlot> getOutputSlots() { return null; }
    default List<ItemSlot> getSlotsFromDirection(Direction direction) { return null; }
    default ISlotContainer getContainer() { return null; }
}