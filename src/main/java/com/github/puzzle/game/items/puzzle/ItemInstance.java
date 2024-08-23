package com.github.puzzle.game.items.puzzle;

import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.util.Reflection;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.items.ItemStack;

public class ItemInstance implements Item {

    final Item parentItem;
    final ItemStack parentStack;

    public ItemInstance(ItemStack parent) {
        if (parent == null) {
            parentItem = null;
            parentStack = null;
        } else {
            parentItem = Reflection.getFieldContents(parent, "item");
            parentStack = parent;
        }
    }

    @Override
    public String getID() {
        if (parentItem == null) return "puzzle-loader:item_instance";
        return parentItem.getID();
    }

    @Override
    public boolean canMergeWith(Item item) {
        if (parentItem == null) return true;
        return parentItem.canMergeWith(item);
    }

    @Override
    public boolean canMergeWithSwapGroup(Item item) {
        if (parentItem == null) return true;
        return parentItem.canMergeWithSwapGroup(item);
    }

    @Override
    public Item getNextSwapGroupItem() {
        if (parentItem == null) return this;
        return parentItem.getNextSwapGroupItem();
    }

    @Override
    public boolean isCatalogHidden() {
        if (parentItem == null) return true;
        return parentItem.isCatalogHidden();
    }

    @Override
    public boolean useItem(ItemSlot itemSlot, Player player) {
        if (parentItem == null) return false;
        return parentItem.useItem(itemSlot, player);
    }

    @Override
    public int getDefaultStackLimit() {
        if (parentItem == null) return 100;
        return parentItem.getDefaultStackLimit();
    }

    @Override
    public float getEffectiveBreakingSpeed(ItemStack itemStack) {
        if (parentItem == null) return 1;
        return parentItem.getEffectiveBreakingSpeed(itemStack);
    }

    @Override
    public boolean isEffectiveBreaking(ItemStack itemStack, BlockState blockState) {
        if (parentItem == null) return false;
        return parentItem.isEffectiveBreaking(itemStack, blockState);
    }

    @Override
    public boolean hasIntProperty(String s) {
        if (parentItem == null) return false;
        if (parentItem instanceof IModItem) {
            return ((IModItem) parentItem).hasIntProperty(parentStack, s);
        }
        return parentItem.hasIntProperty(s);
    }

    @Override
    public int getIntProperty(String s, int i) {
        if (parentItem == null) return 0;
        if (parentItem instanceof IModItem) {
            return ((IModItem) parentItem).getIntProperty(parentStack, s, i);
        }
        return parentItem.getIntProperty(s, i);
    }

    public Item getParentItem() {
        return parentItem;
    }

    public ItemStack getParentStack() {
        return parentStack;
    }

    @Override
    public String toString() {
        if (parentItem == null) return getID();
        return parentItem.toString();
    }
}
