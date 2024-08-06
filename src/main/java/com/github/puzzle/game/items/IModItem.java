package com.github.puzzle.game.items;

import com.github.puzzle.core.Identifier;
import com.github.puzzle.core.Puzzle;
import com.github.puzzle.core.resources.ResourceLocation;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemStack;

public interface IModItem extends Item {

    Identifier getIdentifier();

    default String getID() {
        return getIdentifier().toString();
    }

    default ItemStack getDefaultItemStack() {
        return new ItemStack(this, 100);
    }

    static <T extends IModItem> T registerItem(T item) {
        allItems.put(item.getID(), item);
        return item;
    }

    default boolean canMergeWithSwapGroup(Item item) {
        if (item.getID().equals(this.getID())) {
            return item.getClass().getName().equals(this.getClass().getName());
        }
        return false;
    }

    default boolean canMergeWith(Item item) {
        if (item.getID().equals(this.getID())) {
            return item.getClass().getName().equals(this.getClass().getName());
        }
        return false;
    }

    default ResourceLocation getTexturePath() {
        return new ResourceLocation(Puzzle.MOD_ID, "textures/items/null_stick.png");
    }

}
