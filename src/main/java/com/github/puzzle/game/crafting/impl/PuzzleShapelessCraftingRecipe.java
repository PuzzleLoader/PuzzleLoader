package com.github.puzzle.game.crafting.impl;

import finalforeach.cosmicreach.util.Identifier;
import com.github.puzzle.game.crafting.IPuzzleCraftingRecipe;
import com.github.puzzle.game.crafting.IRecipeSerializer;
import com.github.puzzle.game.crafting.RecipeInput;
import com.github.puzzle.game.oredict.tags.Tag;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemStack;
import org.hjson.JsonObject;
import org.hjson.JsonValue;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PuzzleShapelessCraftingRecipe implements IPuzzleCraftingRecipe {

    Identifier recipeType = Identifier.of("base", "shaped_crafting");

    RecipeInput[] inputs;
    ItemStack result;

    @Override
    public Identifier getType() {
        return recipeType;
    }

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

    private boolean match(List<ItemStack> stacks) {
        Map<Integer, Boolean> matchedSlotNumbers = new HashMap<>();

        for (int inputNum = 0; inputNum < inputs.length; inputNum++) {
            RecipeInput input = inputs[0];
            for (int i = 0; i < stacks.size(); i++) {
                ItemStack stack = stacks.get(i);
                if (!matchedSlotNumbers.containsKey(i)) {
                    matchedSlotNumbers.put(i, input.matches(stack));
                } else {
                    if (!matchedSlotNumbers.get(i)) matchedSlotNumbers.put(i, input.matches(stack));
                }
            }
        }
        boolean allAreMatched = false;
        for (boolean b : matchedSlotNumbers.values()) {
            if (!b) {
                allAreMatched = false;
                break;
            } else allAreMatched = true;
        }
        return allAreMatched;
    }

    @Override
    public @Nullable ItemStack assemble(List<ItemStack> items) {
        if (match(items)) {
            return getOutput();
        }
        return null;
    }

    public static class PuzzleShapelessCraftingSerializer implements IRecipeSerializer<PuzzleShapelessCraftingRecipe>  {

        @Override
        public PuzzleShapelessCraftingRecipe readRecipe(JsonObject object) {
            PuzzleShapelessCraftingRecipe recipe = new PuzzleShapelessCraftingRecipe();
            recipe.recipeType = Identifier.of(object.getString("type", "base:invalid_recipe"));

            List<RecipeInput> recipeInputs = new ArrayList<>();
            for (JsonValue ingredient : object.get("ingredients").asArray()) {
                JsonObject ingredientObj = ingredient.asObject();
                String item = ingredientObj.getString("item", "base:air");
                int count = ingredientObj.getInt("count", 1);
                RecipeInput input;
                if (Tag.tagExist(item)) {
                    input = new RecipeInput(Tag.of(item), count);
                } else input = new RecipeInput(Item.getItem(item), count);
                recipeInputs.add(input);
            }
            recipe.inputs = recipeInputs.toArray(new RecipeInput[0]);

            recipe.result = new ItemStack(Item.getItem(object.getString("id", "base:air")), object.getInt("count", 1));
            return recipe;
        }

        @Override
        public String writeRecipeToJson(PuzzleShapelessCraftingRecipe recipe) {
            StringBuilder builder = new StringBuilder();
            builder.append("{ \"type\": \"" + recipe.recipeType + "\", ");
            builder.append("\"ingredients\": [");
            for (int i = 0; i < recipe.inputs.length; i++) {
                RecipeInput input = recipe.inputs[i];
                builder.append(input.toString());
                if (i < recipe.inputs.length - 1) {
                    builder.append(", ");
                }
            }
            builder.append("]");
            builder.append("\"result\": { \"count\": \""+recipe.result.amount+"\", \"id\":\""+recipe.result.getItem().getID()+"\" }}");
            return builder.toString();
        }
    }
}

