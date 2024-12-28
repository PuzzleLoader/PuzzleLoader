package com.github.puzzle.game.mixins.common.blockentities;

import com.github.puzzle.game.items.IStorageContainer;
import finalforeach.cosmicreach.blockentities.BlockEntityItemContainer;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.items.containers.SlotContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(BlockEntityItemContainer.class)
public class BlockEntityItemContainerMixin implements IStorageContainer {

    @Shadow
    public SlotContainer slotContainer;

    @Override
    public List<ItemSlot> getInputSlots() {
        List<ItemSlot> itemSlotList = new ArrayList<>();
        slotContainer.forEachSlot(itemSlotList::add);
        return itemSlotList;
    }

    @Override
    public List<ItemSlot> getOutputSlots() {
        List<ItemSlot> itemSlotList = new ArrayList<>();
        slotContainer.forEachSlot(itemSlotList::add);
        return itemSlotList;
    }
}
