package com.github.puzzle.game.mixins.refactors.items;

import com.github.puzzle.game.items.IModItem;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.items.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemSlot.class)
public class ItemSlotMixin {

    @Shadow public ItemStack itemStack;

    /**
     * @author Mr_Zombii
     * @reason Allow Max Stack Size
     */
    @Overwrite
    public boolean merge(ItemStack stackFrom) {
        ItemSlot toSlot = (ItemSlot) (Object) this;
        ItemStack stackTo = toSlot.itemStack;
        if (stackTo == stackFrom) {
            return true;
        } else if (stackTo != null && stackFrom == null) {
            return false;
        } else if (stackTo == null && stackFrom != null) {
            toSlot.itemStack = stackFrom.copy();
            stackFrom.amount = 0;
            return true;
        } else if (stackTo.getItem().canMergeWith(stackFrom.getItem()) && stackFrom.getItem().canMergeWith(stackTo.getItem())) {
            int newAmount = Math.min(stackTo.stackLimit, stackTo.amount + stackFrom.amount);
            if (stackFrom.getItem() instanceof IModItem modItem) {
                if (newAmount > modItem.getMaxStackSize()) {
                    return false;
                }
            }
            stackFrom.amount -= newAmount - stackTo.amount;
            stackTo.amount = newAmount;
            return true;
        } else {
            return false;
        }
    }

    /**
     * @author Mr_Zombii
     * @reason Allow Max Stack Size
     */
    @Overwrite
    public boolean mergeInto(ItemSlot targetSlot) {
        ItemSlot fromSlot = (ItemSlot) (Object) this;
        ItemStack stackFrom = fromSlot.itemStack;
        ItemStack stackTarget = targetSlot.itemStack;
        if (stackFrom == stackTarget) {
            return true;
        } else if (stackFrom != null && stackTarget == null) {
            return false;
        } else if (stackFrom == null && stackTarget != null) {
            return false;
        } else if (stackFrom.getItem().getClass() != stackTarget.getItem().getClass()) {
            return false;
        } else if (stackFrom.getItem().canMergeWith(stackTarget.getItem()) && stackTarget.getItem().canMergeWith(stackFrom.getItem())) {
            int newAmount = Math.min(stackFrom.stackLimit, stackFrom.amount + stackTarget.amount);
            if (stackTarget.getItem() instanceof IModItem modItem) {
                if (newAmount > modItem.getMaxStackSize()) {
                    return false;
                }
            }
            stackFrom.amount -= newAmount - stackTarget.amount;
            stackTarget.amount = newAmount;
            if (stackFrom.amount <= 0) {
                fromSlot.itemStack = null;
            }

            return true;
        } else {
            return false;
        }
    }

}
