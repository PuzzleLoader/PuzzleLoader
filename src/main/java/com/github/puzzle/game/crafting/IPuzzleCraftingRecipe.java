package com.github.puzzle.game.crafting;

import com.github.puzzle.core.Identifier;
import finalforeach.cosmicreach.items.ItemStack;
import org.hjson.JsonObject;

import java.util.*;

// TODO: Add recipe docs
public interface IPuzzleCraftingRecipe {

    Map<String, Set<IPuzzleCraftingRecipe>> recipes = new HashMap<>();
    Map<String, IRecipeSerializer<?>> recipeDeserializers = new HashMap<>();

    static Set<IPuzzleCraftingRecipe> getRecipesOfType(Identifier id) {
        Set<IPuzzleCraftingRecipe> recipes1 = recipes.get(id.toString());
        return recipes1 == null ? new HashSet<>() : recipes.get(id.toString());
    }

    static <T extends IPuzzleCraftingRecipe> void registerRecipeType(Identifier id, IRecipeSerializer<T> deserializer) {
        if (recipeDeserializers.containsKey(id.toString())) throw new RuntimeException("Recipe Serializer of type \"" + id.toString() + "\" has been register 2+ times");
        recipeDeserializers.put(id.toString(), deserializer);
    }

    static void addRecipe(Identifier id, String recipeJson) {
        JsonObject object = JsonObject.readHjson(recipeJson).asObject();
        IRecipeSerializer<?> recipeSerializer = recipeDeserializers.get(object.get("type").asString());

        if (recipeSerializer == null) throw new RuntimeException("Serializer for recipe of type \""+id.toString()+"\" does not exist");
        addRecipe(id, recipeSerializer.readRecipe(object));
    }

    static void addRecipe(Identifier id, IPuzzleCraftingRecipe recipe) {
        Set<IPuzzleCraftingRecipe> recipes1;
        if (recipes.get(id.toString()) == null)
            recipes1 = new HashSet<>();
        else
            recipes1 = recipes.get(id.toString());
        recipes1.add(recipe);
        recipes.put(id.toString(), recipes1);
    }

    RecipeInput[] getInputs();
    ItemStack getOutput();

    ItemStack assemble(List<ItemStack> items);

}
