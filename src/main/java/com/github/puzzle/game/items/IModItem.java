package com.github.puzzle.game.items;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.utils.Array;
import com.github.puzzle.core.Identifier;
import com.github.puzzle.core.Puzzle;
import com.github.puzzle.core.resources.ResourceLocation;
import com.github.puzzle.game.engine.items.PuzzleItemModel;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemStack;

import static finalforeach.cosmicreach.rendering.items.ItemRenderer.registerItemModelCreator;

public interface IModItem extends Item {

    /**
     * The identifier for your custom item.
     * @see Identifier
     */
    Identifier getIdentifier();

    /**
     * The string version of the ID.
     * @see Identifier#toString
     */
    default String getID() {
        return getIdentifier().toString();
    }

    /**
     * A method to create the default itemStack the comes with your item.
     */
    default ItemStack getDefaultItemStack() {
        return new ItemStack(this, 100);
    }

    /**
     * A mesh the will come with your item if you decide 2d isn't enough.
     * @see PuzzleItemModel
     */
    default Array<Mesh> getMesh() {
        return null;
    }

    /**
     * A simple method to register your item with the vanilla game for rendering and referencing.
     * @see PuzzleItemModel
     * @see Item#allItems
     * @see finalforeach.cosmicreach.rendering.items.ItemRenderer#registerItemModelCreator
     */
    static <T extends IModItem> T registerItem(T item) {
        allItems.put(item.getID(), item);

        registerItemModelCreator(item.getClass(), (modItem) -> {
            return new PuzzleItemModel(modItem.get());
        });

        return item;
    }

    /**
     * A method to allow you to merge with other itemStacks of the same type,
     * this method is normally used when an item/block has extra data on it,
     * like blockStates
     * @see finalforeach.cosmicreach.blocks.BlockState
     * @see ItemStack
     * @see finalforeach.cosmicreach.items.ItemBlock#canMergeWithSwapGroup(Item)
     */
    default boolean canMergeWithSwapGroup(Item item) {
        if (item.getID().equals(this.getID())) {
            return item.getClass().getName().equals(this.getClass().getName());
        }
        return false;
    }

    /**
     * A method to allow you to merge with other itemStacks of the same type.
     * @see ItemStack
     * @see finalforeach.cosmicreach.items.ItemBlock#canMergeWith(Item) (Item)
     */
    default boolean canMergeWith(Item item) {
        if (item.getID().equals(this.getID())) {
            return item.getClass().getName().equals(this.getClass().getName());
        }
        return false;
    }

    /**
     * The path to the texture your item uses
     * @see com.badlogic.gdx.graphics.Texture
     * @see ResourceLocation
     */
    default ResourceLocation getTexturePath() {
        return new ResourceLocation(Puzzle.MOD_ID, "textures/items/null_stick.png");
    }

}
