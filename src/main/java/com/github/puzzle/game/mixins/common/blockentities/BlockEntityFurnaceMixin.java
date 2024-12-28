package com.github.puzzle.game.mixins.common.blockentities;

import com.github.puzzle.game.items.IStorageContainer;
import finalforeach.cosmicreach.blockentities.BlockEntityFurnace;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.items.containers.FurnaceSlotContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(BlockEntityFurnace.class)
public class BlockEntityFurnaceMixin implements IStorageContainer {

    @Shadow
    public FurnaceSlotContainer slotContainer;

    @Override
    public List<ItemSlot> getInputSlots() {
        List<ItemSlot> itemSlotList = new ArrayList<>(2);
        itemSlotList.add(slotContainer.getFuelSlot());
        itemSlotList.add(slotContainer.getIngredientSlot());
        return itemSlotList;
    }

    @Override
    public List<ItemSlot> getOutputSlots() {
        List<ItemSlot> itemSlotList = new ArrayList<>(2);
        itemSlotList.add(slotContainer.getOutputProductSlot());
        itemSlotList.add(slotContainer.getOutputByProductSlot());
        return itemSlotList;
    }
}
