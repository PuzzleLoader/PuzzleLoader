package com.github.puzzle.game.crafting;

import org.hjson.JsonObject;

public interface IRecipeSerializer<T extends IPuzzleCraftingRecipe> {

    T readRecipe(JsonObject object);
    String writeRecipeToJson(T recipe);

}
