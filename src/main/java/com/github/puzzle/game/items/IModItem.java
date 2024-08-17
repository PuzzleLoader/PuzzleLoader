package com.github.puzzle.game.items;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.puzzle.core.Identifier;
import com.github.puzzle.core.Puzzle;
import com.github.puzzle.core.resources.ResourceLocation;
import com.github.puzzle.game.engine.items.PuzzleItemModel;
import com.github.puzzle.game.items.data.DataTag;
import com.github.puzzle.game.items.data.DataTagManifest;
import com.github.puzzle.game.items.data.DataTagPreset;
import com.github.puzzle.game.items.data.attributes.BooleanDataAttribute;
import com.github.puzzle.game.items.data.attributes.IdentifierDataAttribute;
import com.github.puzzle.game.items.data.attributes.ResourceLocationDataAttribute;
import com.github.puzzle.game.mixins.accessors.ItemRenderAccessor;
import com.github.puzzle.game.util.Reflection;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.rendering.items.ItemModel;
import finalforeach.cosmicreach.rendering.items.ItemRenderer;

import java.lang.ref.WeakReference;
import java.util.function.Function;

import static finalforeach.cosmicreach.rendering.items.ItemRenderer.registerItemModelCreator;

public interface IModItem extends Item {

    /**
     * The identifier for your custom item.
     * @see Identifier
     */
    Identifier getIdentifier();

    DataTagPreset<Identifier> MODEL_ID_PRESET = new DataTagPreset<>("model_id", new IdentifierDataAttribute(Identifier.of(Puzzle.MOD_ID, "2d_item_model")));
    Identifier MODEL_2D_ITEM = new Identifier(Puzzle.MOD_ID, "2d_item_model");
    Identifier MODEL_2_5D_ITEM = new Identifier(Puzzle.MOD_ID, "2.5d_item_model");
    Identifier MODEL_USE_CUSTOM_MODEL = new Identifier(Puzzle.MOD_ID, "CUSTOM_3D_MODEL");

    DataTagPreset<ResourceLocation> TEXTURE_LOCATION_PRESET = new DataTagPreset<>("texture_resource_location", new ResourceLocationDataAttribute(new ResourceLocation(Puzzle.MOD_ID, "textures/items/null_stick.png")));

    DataTagPreset<Boolean> IS_DEBUG_ATTRIBUTE = new DataTagPreset<>("is_item_debug", new BooleanDataAttribute(false));

    /**
     * The string version of the ID.
     * @see Identifier#toString
     */
    default String getID() {
        return getIdentifier().toString();
    }

    /**
     * This allows your item to be used by the player.
     * This method is a remap/rename of useItem
     * @see IModItem#useItem(ItemSlot, Player)
     * @see Item#useItem(ItemSlot, Player)
     */
    default void use(ItemSlot slot, Player player) {
    }


    /**
     * This is a method that makes your item usable by the player.
     *
     * @deprecated impl the "use" method instead for a cleaner look
     * in your code.
     * @see IModItem#use(ItemSlot, Player)
     */
    @Deprecated
    default boolean useItem(ItemSlot slot, Player player) {
        return false;
    }

    /**
     * This gets the breaking speed for blocks that it was made for.
     * @see Item#getEffectiveBreakingSpeed(ItemStack)
     */
    default float getEffectiveBreakingSpeed(ItemStack stack) {
        return 1f;
    }

    /**
     * This is a method that is used for checking what blocks this tool is ment to break.
     * @see Item#isEffectiveBreaking(ItemStack, BlockState)
     */
    default boolean isEffectiveBreaking(ItemStack itemStack, BlockState blockState) {
        return false;
    }

    /**
     * This was ment to give the default stack size and make it only stack to X amount.
     *
     * @deprecated use getDefaultItemStack and getMaxStackSize instead.
     * @see IModItem#getDefaultItemStack()
     * @see IModItem#getMaxStackSize()
     */
    @Deprecated
    default int getDefaultStackLimit() {
        return getMaxStackSize();
    }

    /**
     * A method to create the default itemStack the comes with your item.
     */
    default ItemStack getDefaultItemStack() {
        return new ItemStack(this, Math.min(getMaxStackSize(), 100));
    }

    /**
     * A method that returns the max stackable size for this item type.
     * @see ItemStack
     */
    default int getMaxStackSize() {
        return 1000;
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

        ItemRenderAccessor.getRefMap().put(item, new WeakReference<>(item));
        ObjectMap<Class<? extends Item>, Function<?, ItemModel>> modelCreators = Reflection.getFieldContents(ItemRenderer.class, "modelCreators");

        if (!modelCreators.containsKey(item.getClass())) {
            registerItemModelCreator(item.getClass(), (modItem) -> {
                return new PuzzleItemModel(modItem.get());
            });
        }

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
     * This bool changes how the item is held in the "hand".
     * @see PuzzleItemModel
     */
    default boolean isTool() {
        return false;
    }

    /**
     * This related to custom data that you can attach to your item.
     * @see DataTag
     * @see com.github.puzzle.game.items.data.DataTag.DataTagAttribute
     */
    default DataTagManifest getTagManifest() {
        return new DataTagManifest();
    }

    default boolean hasIntProperty(String s) {
        if (getTagManifest() == null) return false;
        return getTagManifest().hasTag(s);
    }

    default int getIntProperty(String s, int i) {
        if (getTagManifest() == null) return i;
        if (getTagManifest().hasTag(s)) return (int) getTagManifest().getTag(s).getValue();
        return i;
    }

}
