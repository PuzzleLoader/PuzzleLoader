package com.github.puzzle.game.crafting.impl;

import finalforeach.cosmicreach.util.Identifier;
import com.github.puzzle.game.crafting.IPuzzleCraftingRecipe;
import com.github.puzzle.game.crafting.IRecipeSerializer;
import com.github.puzzle.game.crafting.RecipeInput;
import com.github.puzzle.game.oredict.tags.Tag;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.ImmutablePair;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemStack;
import org.hjson.JsonArray;
import org.hjson.JsonObject;
import org.hjson.JsonValue;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PuzzleShapedCraftingRecipe implements IPuzzleCraftingRecipe {

    Identifier recipeType = Identifier.of("base", "shaped_crafting");
    Map<String, RecipeInput> symbolTable = new HashMap<>();

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
            recipe.recipeType = Identifier.of(object.getString("type", "base:invalid_recipe"));
            Pair<Map<String, RecipeInput>, RecipeInput[]> inputs = patternToRecipe(object.get("pattern").asArray(), object.get("key").asObject());
            recipe.inputs = inputs.getRight();
            recipe.symbolTable = inputs.getLeft();
            recipe.result = new ItemStack(Item.getItem(object.getString("id", "base:air")), object.getInt("count", 1));
            return recipe;
        }

        public Pair<Map<String, RecipeInput>, RecipeInput[]> patternToRecipe(JsonArray patterns, JsonObject keys) {
            List<RecipeInput> recipeInputs = new ArrayList<>();
            Map<String, RecipeInput> symbolTable = new HashMap<>();

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
                        symbolTable.put(String.valueOf(chr), input);
                        recipeInputs.add(input);
                    }
                }
            }

            return new ImmutablePair<>(symbolTable, recipeInputs.toArray(new RecipeInput[0]));
        }

        @Override
        public String writeRecipeToJson(PuzzleShapedCraftingRecipe recipe) {
            StringBuilder builder = new StringBuilder();
            builder.append("{ \"type\": \"" + recipe.recipeType + "\", ");
            builder.append("\"pattern\": [");
            String[] keyset = recipe.symbolTable.keySet().toArray(new String[0]);
            builder.append("\"" + keyset[0] + keyset[1] + keyset[2] + "\", ");
            builder.append("\"" + keyset[3] + keyset[4] + keyset[5] + "\", ");
            builder.append("\"" + keyset[6] + keyset[7] + keyset[8] + "\" ], ");

            builder.append("\"key\": {");
            for (int i = 0; i < recipe.symbolTable.keySet().size(); i++) {
                String key = recipe.symbolTable.keySet().toArray(new String[0])[i];
                RecipeInput input = recipe.symbolTable.values().toArray(new RecipeInput[0])[i];
                builder.append("\"" + key + "\": " + input.toString());
                if (i < recipe.symbolTable.keySet().size() - 1) {
                    builder.append(", ");
                }
            }
            builder.append("},");
            builder.append("\"result\": { \"count\": \""+recipe.result.amount+"\", \"id\":\""+recipe.result.getItem().getID()+"\" }}");
            return builder.toString();
        }
    }
}