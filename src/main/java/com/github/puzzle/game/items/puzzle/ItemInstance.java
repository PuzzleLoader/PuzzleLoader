package com.github.puzzle.game.items.puzzle;

import com.badlogic.gdx.graphics.Mesh;
import com.github.puzzle.core.Identifier;
import com.github.puzzle.core.resources.ResourceLocation;
import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.data.DataTag;
import com.github.puzzle.game.items.data.DataTagManifest;
import com.github.puzzle.game.items.data.attributes.IdentifierDataAttribute;
import com.github.puzzle.game.items.data.attributes.ListDataAttribute;
import com.github.puzzle.game.items.data.attributes.PairAttribute;
import com.github.puzzle.game.items.data.attributes.ResourceLocationDataAttribute;
import com.github.puzzle.game.util.Reflection;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.items.ItemThing;

import java.util.ArrayList;
import java.util.List;

public class ItemInstance implements IModItem {

    final Item parentItem;
    final ItemStack parentStack;

    DataTagManifest manifest = new DataTagManifest();

    public ItemInstance(ItemStack parent) {
        if (parent == null) {
            parentItem = null;
            parentStack = null;
        } else {
            parentItem = Reflection.getFieldContents(parent, "item");
            parentStack = parent;
        }
    }

    Identifier id = Identifier.fromString("puzzle-loader:item_instance");

    @Override
    public Identifier getIdentifier() {
        if (parentItem == null) return id;
        return Identifier.fromString(parentItem.getID());
    }

    public void addTexture(Identifier model, ResourceLocation texture) {
        if (parentItem == null) {
            if (getTagManifest().hasTag("textures")) {
                ListDataAttribute<PairAttribute<IdentifierDataAttribute, ResourceLocationDataAttribute>> textures = (ListDataAttribute) getTagManifest().getTag("textures").attribute;
                List<PairAttribute<IdentifierDataAttribute, ResourceLocationDataAttribute>> attributes = textures.getValue();
                attributes.add(new PairAttribute<>(new IdentifierDataAttribute(model), new ResourceLocationDataAttribute(texture)));
                textures.setValue(attributes);
                getTagManifest().addTag(new DataTag<>("textures", textures));
            } else {
                List<PairAttribute<IdentifierDataAttribute, ResourceLocationDataAttribute>> attributes = new ArrayList<>();
                attributes.add(new PairAttribute<>(new IdentifierDataAttribute(model), new ResourceLocationDataAttribute(texture)));
                getTagManifest().addTag(new DataTag<>("textures", new ListDataAttribute<>(attributes)));
            }
        } else if (IModItem.class.isAssignableFrom(parentItem.getClass()))
            ((IModItem) parentItem).addTexture(model, texture);
    }

    public void addTexture(Identifier model, ResourceLocation... textures) {
        if (parentItem == null) {
            for (ResourceLocation location : textures) {
                addTexture(model, location);
            }
        } else if (IModItem.class.isAssignableFrom(parentItem.getClass())) ((IModItem) parentItem).addTexture(model, textures);

    }

    public List<PairAttribute<IdentifierDataAttribute, ResourceLocationDataAttribute>> getTextures() {
        if (parentItem == null) {
            if (getTagManifest().hasTag("textures")) {
                ListDataAttribute<PairAttribute<IdentifierDataAttribute, ResourceLocationDataAttribute>> textures = (ListDataAttribute) getTagManifest().getTag("textures").attribute;
                return textures.getValue();
            }
            return new ArrayList<>();
        } else if (IModItem.class.isAssignableFrom(parentItem.getClass())) return ((IModItem) parentItem).getTextures();
        return new ArrayList<>();
    }

    public String getID() {
        if (parentItem == null) return id.toString();
        return parentItem.getID();
    }

    public void use(ItemSlot slot, Player player) {
        if (parentItem == null) return;
        else if (IModItem.class.isAssignableFrom(parentItem.getClass())) ((IModItem) parentItem).use(slot, player);
    }

    public void use(ItemSlot slot, Player player, boolean isLeftClick) {
        if (parentItem == null) return;
        else if (IModItem.class.isAssignableFrom(parentItem.getClass())) ((IModItem) parentItem).use(slot, player, isLeftClick);
    }

    public boolean useItem(ItemSlot slot, Player player) {
        if (parentItem == null) return false;
        return parentItem.useItem(slot, player);
    }

    public float getEffectiveBreakingSpeed(ItemStack stack) {
        if (parentItem == null) return 1f;
        return parentItem.getEffectiveBreakingSpeed(stack);
    }

    public boolean isEffectiveBreaking(ItemStack itemStack, BlockState blockState) {
        if (parentItem == null) return false;
        return parentItem.isEffectiveBreaking(itemStack, blockState);
    }

    public int getDefaultStackLimit() {
        if (parentItem == null) return 100;
        return parentItem.getDefaultStackLimit();
    }

    public ItemStack getDefaultItemStack() {
        if (parentItem == null) return new ItemStack(this, Math.min(getMaxStackSize(), 100));
        else if (IModItem.class.isAssignableFrom(parentItem.getClass())) return ((IModItem) parentItem).getDefaultItemStack();
        return new ItemStack(this, Math.min(getMaxStackSize(), 100));
    }

    public int getMaxStackSize() {
        if (parentItem == null) return 1000;
        else if (IModItem.class.isAssignableFrom(parentItem.getClass())) return ((IModItem) parentItem).getMaxStackSize();
        return 1000;
    }

    public boolean canMergeWithSwapGroup(Item item) {
        if (parentItem == null) {
            if (item.getID().equals(this.getID())) {
                return item.getClass().getName().equals(this.getClass().getName());
            }
            return false;
        }
        return parentItem.canMergeWithSwapGroup(item);
    }

    @Override
    public Item getNextSwapGroupItem() {
        if (parentItem == null) return this;
        return parentItem.getNextSwapGroupItem();
    }

    public boolean canMergeWith(Item item) {
        if (parentItem == null) {
            if (item.getID().equals(this.getID())) {
                return item.getClass().getName().equals(this.getClass().getName());
            }
            return false;
        }
        return parentItem.canMergeWith(item);
    }

    @Override
    public boolean isTool() {
        if (parentItem == null) return false;
        else if (IModItem.class.isAssignableFrom(parentItem.getClass())) return ((IModItem) parentItem).isTool();
        return false;
    }

    public DataTagManifest getTagManifest() {
        if (parentItem == null) return manifest;
        else if (IModItem.class.isAssignableFrom(parentItem.getClass())) return ((IModItem) parentItem).getTagManifest();
        return manifest;
    }

    public boolean hasIntProperty(String s) {
        if (parentItem == null) return false;
        return parentItem.hasIntProperty(s);
    }

    public int getIntProperty(String s, int i) {
        if (parentItem == null) return 0;
        return parentItem.getIntProperty(s, i);
    }

    public boolean hasIntProperty(ItemStack parent, String s) {
        if (parentItem == null) return false;
        else if (IModItem.class.isAssignableFrom(parentItem.getClass())) return ((IModItem) parentItem).hasIntProperty(parent, s);
        return parentItem.hasIntProperty(s);
    }

    public int getIntProperty(ItemStack parent, String s, int i) {
        if (parentItem == null) return 0;
        else if (IModItem.class.isAssignableFrom(parentItem.getClass())) return ((IModItem) parentItem).getIntProperty(parent, s, i);
        return parentItem.getIntProperty(s, i);
    }

    public boolean isCatalogHidden() {
        if (parentItem == null) return true;
        return parentItem.isCatalogHidden();
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
