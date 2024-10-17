package com.github.puzzle.game.util;

import com.github.puzzle.game.items.data.DataTagManifest;
import com.github.puzzle.game.items.stack.ITaggedStack;
import finalforeach.cosmicreach.items.ItemStack;

public class DataTagUtil {

    public static DataTagManifest getManifestFromStack(ItemStack stack) {
        if (stack instanceof ITaggedStack taggedStack) {
            return taggedStack.puzzleLoader$getDataManifest();
        }
        throw new RuntimeException("INVALID ITEM STACK, ALL STACKS MUST IMPLEMENT com.github.puzzle.game.items.stack.ITaggedStack");
    }

    public static ItemStack setManifestOnStack(DataTagManifest tagManifest, ItemStack stack) {
        if (stack instanceof ITaggedStack taggedStack) {
            taggedStack.puzzleLoader$setDataManifest(tagManifest);
        } else throw new RuntimeException("INVALID ITEM STACK, ALL STACKS MUST IMPLEMENT com.github.puzzle.game.items.stack.ITaggedStack");
        return stack;
    }

}
