package com.github.puzzle.game.mixins.refactors.blockentities;

import com.github.puzzle.game.items.IStorageContainer;
import finalforeach.cosmicreach.blockentities.BlockEntityFurnace;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.items.containers.FurnaceSlotContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockEntityFurnace.class)
public class BlockEntityFurnaceMixin implements IStorageContainer {

    @Shadow
    public FurnaceSlotContainer slotContainer;

    @Override
    public ItemSlot[] getInputSlots() {
        ItemSlot[] Slots = new ItemSlot[2];
        Slots[0] = slotContainer.getFuelSlot();
        Slots[1] = slotContainer.getIngredientSlot();
        return Slots;
    }

    @Override
    public ItemSlot[] getOutputSlots() {
        ItemSlot[] Slots = new ItemSlot[2];
        Slots[0] = slotContainer.getOutputProductSlot();
        Slots[1] = slotContainer.getOutputByProductSlot();
        return Slots;
    }
}
