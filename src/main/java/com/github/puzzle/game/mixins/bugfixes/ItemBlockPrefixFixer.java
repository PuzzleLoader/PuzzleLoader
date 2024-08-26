package com.github.puzzle.game.mixins.bugfixes;

import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemBlock;
import finalforeach.cosmicreach.util.exceptions.DuplicateIDException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static finalforeach.cosmicreach.items.Item.allItems;

@Mixin(ItemBlock.class)
public class ItemBlockPrefixFixer {

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/items/Item;registerItem(Lfinalforeach/cosmicreach/items/Item;)V"))
    private void registerItem(Item item) {
        String itemId = item.getID();
        if (itemId == null) {
            throw new RuntimeException("Item id cannot be null!");
        } else if (itemId.length() == 0) {
            throw new RuntimeException("Item id cannot be empty!");
        } else if (!itemId.contains(":")) {
            itemId = "base:" + itemId;
            allItems.put(itemId, item);
            System.out.println("Registered item id: " + itemId);
        } else if (allItems.containsKey(itemId)) {
            throw new DuplicateIDException("Duplicate item for id: " + item.getID());
        } else {
            allItems.put(itemId, item);
            System.out.println("Registered item id: " + item.getID());
        }
    }

}
