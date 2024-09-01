package com.github.puzzle.game.crafting;

import com.github.puzzle.game.oredict.ResourceDictionary;
import com.github.puzzle.game.oredict.tags.Tag;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.ImmutablePair;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemStack;

public class RecipeInput {

    private final Tag tag;
    private final Item item;
    private final int count;

    public RecipeInput(ItemStack stack) {
        this.item = stack.getItem();
        this.count = stack.amount;
        this.tag = null;
    }

    public RecipeInput(Item item) {
        this.item = item;
        this.count = 1;
        this.tag = null;
    }

    public RecipeInput(Item item, int count) {
        this.item = item;
        this.count = count;
        this.tag = null;
    }

    public RecipeInput(Tag tag) {
        this.tag = tag;
        this.count = 1;
        this.item = null;
    }

    public RecipeInput(Tag tag, int count) {
        this.tag = tag;
        this.count = count;
        this.item = null;
    }

    public boolean matches(Item item) {
        if (item != null)
            return matches(new ItemStack(item, 1));
        return false;
    }

    public boolean matches(ItemStack itemstack) {
        if (item == null) {
            if (ResourceDictionary.getItemsFromTag(tag).contains(itemstack.getItem())) {
                return count == itemstack.amount;
            }
            return false;
        }
        assert item != null;
        if (item.equals(itemstack)) {
            return count == itemstack.amount;
        }
        return false;
    }

    boolean isTag() {
        return tag != null;
    }

    public Pair<Integer, Tag> getTagIntPair() {
        return new ImmutablePair<>(count, tag);
    }

    public ItemStack getItem() {
        return new ItemStack(item, count);
    }

    @Override
    public String toString() {
        if (isTag()) return "{ \"count\": \""+count+"\", \"item\":\""+tag.toString()+"\" }";
        return "{ \"count\": \""+count+"\", \"item\":\""+item.getID()+"\" }";
    }

}
