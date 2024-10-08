package com.github.puzzle.game.items;

import com.badlogic.gdx.utils.ObjectMap;
import com.github.puzzle.core.Constants;
import com.github.puzzle.core.loader.meta.Environment;
import com.github.puzzle.core.loader.util.Reflection;
import com.github.puzzle.game.items.data.DataTag;
import com.github.puzzle.game.items.data.DataTagManifest;
import com.github.puzzle.game.items.data.DataTagPreset;
import com.github.puzzle.game.items.data.attributes.*;
import com.github.puzzle.game.util.DataTagUtil;
import finalforeach.cosmicreach.blockentities.BlockEntityFurnace;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.util.Identifier;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.puzzle.core.Constants.MOD_ID;

public interface IModItem extends Item {

    /**
     * The identifier for your custom item.
     * @see Identifier
     */
    Identifier getIdentifier();

    static <T extends IModItem> T registerItem(T item) {
        allItems.put(item.getID(), item);

        if (Environment.CLIENT == Constants.SIDE) {
            try {
                Class<?> clazz = Class.forName("com.github.puzzle.game.engine.items.ClientItemRegistrar");
                Method method = Reflection.getMethod(clazz, "registerModel", IModItem.class);
                method.invoke(null, item);

            } catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return item;
    }

    DataTagPreset<Identifier> MODEL_ID_PRESET = new DataTagPreset<>("model_id", new IdentifierDataAttribute(Identifier.of(MOD_ID, "2d_item_model")));
    Identifier MODEL_2D_ITEM = Identifier.of(MOD_ID, "2d_item_model");
    Identifier MODEL_2_5D_ITEM = Identifier.of(MOD_ID, "3d_item_model");

    DataTagPreset<Identifier> TEXTURE_LOCATION_PRESET = new DataTagPreset<>("texture_resource_location", new IdentifierDataAttribute(Identifier.of(MOD_ID, "textures/items/null_stick.png")));

    DataTagPreset<Boolean> IS_DEBUG_ATTRIBUTE = new DataTagPreset<>("is_item_debug", new BooleanDataAttribute(false));

    default void addTexture(Identifier model, Identifier texture) {
        if (getTagManifest().hasTag("textures")) {
            ListDataAttribute<PairAttribute<IdentifierDataAttribute, IdentifierDataAttribute>> textures = (ListDataAttribute) getTagManifest().getTag("textures").attribute;
            List<PairAttribute<IdentifierDataAttribute, IdentifierDataAttribute>> attributes = textures.getValue();
            attributes.add(new PairAttribute<>(new IdentifierDataAttribute(model), new IdentifierDataAttribute(texture)));
            textures.setValue(attributes);
            getTagManifest().addTag(new DataTag<>("textures", textures));
        } else {
            List<PairAttribute<IdentifierDataAttribute, IdentifierDataAttribute>> attributes = new ArrayList<>();
            attributes.add(new PairAttribute<>(new IdentifierDataAttribute(model), new IdentifierDataAttribute(texture)));
            getTagManifest().addTag(new DataTag<>("textures", new ListDataAttribute<>(attributes)));
        }
    }

    default void addTexture(Identifier model, Identifier... textures) {
        for (Identifier location : textures) {
            addTexture(model, location);
        }
    }

    default List<PairAttribute<IdentifierDataAttribute, IdentifierDataAttribute>> getTextures() {
        if (getTagManifest().hasTag("textures")) {
            ListDataAttribute<PairAttribute<IdentifierDataAttribute, IdentifierDataAttribute>> textures = (ListDataAttribute) getTagManifest().getTag("textures").attribute;
            return textures.getValue();
        }
        return new ArrayList<>();
    }

    default void setCurrentEntry(ItemStack stack, int entry) {
        DataTagManifest manifest = DataTagUtil.getManifestFromStack(stack);
        manifest.addTag(new DataTag<>("currentEntry", new IntDataAttribute(entry)));
        DataTagUtil.setManifestOnStack(manifest, stack);
    }

    default int getCurrentEntry(ItemStack stack) {
        DataTagManifest manifest = DataTagUtil.getManifestFromStack(stack);
        if (!manifest.hasTag("currentEntry")) manifest.addTag(new DataTag<>("currentEntry", new IntDataAttribute(0)));
        return manifest.getTag("currentEntry").getTagAsType(Integer.class).getValue();
    }

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
     * This allows your item to be used by the player.
     * This method is a remap/rename of useItem
     * @see IModItem#useItem(ItemSlot, Player)
     * @see Item#useItem(ItemSlot, Player)
     */
    default void use(ItemSlot slot, Player player, boolean isLeftClick) {
        if (!isLeftClick) {
            use(slot, player);
            return;
        }
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
     * A method to allow you to merge with other itemStacks of the same type,
     * this method is normally used when an item/block has extra data on it,
     * like blockStates
     * @see BlockState
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
     * @see com.github.puzzle.game.engine.items.model.IPuzzleItemModel
     */
    default boolean isTool() {
        return false;
    }

    /**
     * This related to custom data that you can attach to your item.
     * @see DataTag
     * @see DataTag.DataTagAttribute
     */
    default DataTagManifest getTagManifest() {
        return new DataTagManifest();
    }

    /* Property Presets */
    DataTagPreset<Integer> FIRE_TICKS = new DataTagPreset<>(BlockEntityFurnace.FUEL_PROPERTY_NAME, new IntDataAttribute(64));
    //    DataTagPreset<Float> TOOL_SPEED = new DataTagPreset<>("toolSpeed", new FloatDataAttribute(4));

    /**
     * Redirected hasIntProperty to use the TagManifest
     * @see DataTagManifest
     */
    default boolean hasIntProperty(String s) {
        if (getTagManifest() == null) return false;
        return getTagManifest().hasTag(s);
    }

    /**
     * Redirected getIntProperty to use the TagManifest
     * @see DataTagManifest
     */
    default int getIntProperty(String s, int i) {
        if (getTagManifest() == null) return i;
        if (getTagManifest().hasTag(s)) return getTagManifest().getTag(s).getTagAsType(Integer.class).getValue();
        return i;
    }

    /**
     * It gets the property from the stack manifest before checking the base item manifest
     * @see DataTagManifest
     */
    default boolean hasIntProperty(ItemStack parent, String s) {
        DataTagManifest manifest = DataTagUtil.getManifestFromStack(parent);
        return manifest.hasTag(s) || hasIntProperty(s);
    }

    /**
     * It gets the property from the stack manifest before checking the base item manifest
     * @see DataTagManifest
     */
    default int getIntProperty(ItemStack parent, String s, int i) {
        DataTagManifest manifest = DataTagUtil.getManifestFromStack(parent);
        if (manifest.hasTag(s)) {
            return manifest.getTag(s).getTagAsType(Integer.class).getValue();
        }
        return getIntProperty(s, i);
    }


    /**
     * Set the default catalog hidden to false
     */
    default boolean isCatalogHidden() {
        return false;
    }

    @Override
    default boolean hasTag(String s) {
        return false;
    }
}
