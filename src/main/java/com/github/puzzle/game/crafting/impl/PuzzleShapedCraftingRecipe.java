package com.github.puzzle.game.crafting.impl;

import com.github.puzzle.core.Identifier;
import com.github.puzzle.core.Puzzle;
import com.github.puzzle.game.crafting.IPuzzleCraftingRecipe;
import com.github.puzzle.game.crafting.IRecipeSerializer;
import com.github.puzzle.game.crafting.RecipeInput;
import com.github.puzzle.game.oredict.tags.Tag;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemStack;
import org.hjson.JsonArray;
import org.hjson.JsonObject;
import org.hjson.JsonValue;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PuzzleShapedCraftingRecipe implements IPuzzleCraftingRecipe {

    Identifier recipeType = new Identifier(Puzzle.MOD_ID, "shaped_crafting");

    RecipeInput[] inputs;
    ItemStack result;

    @Override
    public void setInputs(RecipeInput[] inputs) {
        this.inputs = inputs;
    }

    @Override
    public void setOutput(ItemStack result) {
        this.result = result;
    }

    @Override
    public RecipeInput[] getInputs() {
        return inputs;
    }

    @Override
    public ItemStack getOutput() {
        return result;
    }

    private boolean compareSlot(List<ItemStack> stacks, int slot) {
        if (stacks.get(slot) == null && inputs[slot] == null) return true;
        if (stacks.get(slot) != null && inputs[slot] == null) return false;
        if (stacks.get(slot) == null && inputs[slot] != null) return false;
        return inputs[slot].matches(stacks.get(slot));
    }

    private boolean compareRow(List<ItemStack> stacks, int row) {
        return compareSlot(stacks, row * 3) && compareSlot(stacks, row * 3 + 1) && compareSlot(stacks, row * 3 + 2);
    }

    @Override
    public @Nullable ItemStack assemble(List<ItemStack> items) {
        if (compareRow(items, 0) && compareRow(items, 1) && compareRow(items, 2)) {
            return getOutput();
        }
        return null;
    }

    public static class PuzzleShapedCraftingSerializer implements IRecipeSerializer<PuzzleShapedCraftingRecipe>  {

        @Override
        public PuzzleShapedCraftingRecipe readRecipe(JsonObject object) {
            PuzzleShapedCraftingRecipe recipe = new PuzzleShapedCraftingRecipe();
            recipe.recipeType = Identifier.fromString(object.getString("type", Puzzle.MOD_ID + ":"));
            recipe.inputs = patternToRecipe(object.get("pattern").asArray(), object.get("key").asObject());
            recipe.result = new ItemStack(Item.getItem(object.getString("id", "base:air")), object.getInt("count", 1));
            return recipe;
        }

        public RecipeInput[] patternToRecipe(JsonArray patterns, JsonObject keys) {
            List<RecipeInput> recipeInputs = new ArrayList<>();

            for (JsonValue pattern : patterns) {
                for (char chr : pattern.asString().toCharArray()) {
                    if (keys.get(String.valueOf(chr)) != null) {
                        JsonObject object = keys.get(String.valueOf(chr)).asObject();
                        String item = object.getString("item", "base:air");
                        int count = object.getInt("count", 1);
                        RecipeInput input;
                        if (Tag.tagExist(item)) {
                            input = new RecipeInput(Tag.of(item), count);
                        } else input = new RecipeInput(Item.getItem(item), count);
                        recipeInputs.add(input);
                    }
                }
            }

            return recipeInputs.toArray(new RecipeInput[0]);
        }

        @Override
        public String writeRecipeToJson(PuzzleShapedCraftingRecipe recipe) {
            return "";
        }
    }
}
