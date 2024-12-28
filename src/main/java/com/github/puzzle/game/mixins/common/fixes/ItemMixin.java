package com.github.puzzle.game.mixins.common.fixes;

import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.util.exceptions.DuplicateIDException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static finalforeach.cosmicreach.items.Item.allItems;

@Mixin(Item.class)
public interface ItemMixin {

    /**
     * @author Mr_Zombii
     * @reason Get rid of certain wrappers
     */
    @Overwrite
    static void registerItem(Item item) {
        String itemId = item.getID();
        if (itemId == null) {
            throw new RuntimeException("Item id cannot be null!");
        } else if (itemId.length() == 0) {
            throw new RuntimeException("Item id cannot be empty!");
        } else if (!itemId.contains(":")) {
            itemId = "base:" + itemId;
            allItems.put(itemId, item);
        } else if (allItems.containsKey(itemId)) {
            throw new DuplicateIDException("Duplicate item for id: " + item.getID());
        } else {
            allItems.put(itemId, item);
        }
    }

}
