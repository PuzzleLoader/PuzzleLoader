package com.github.puzzle.game.mixins.bugfixes;

import com.badlogic.gdx.utils.ObjectMap;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.util.exceptions.DuplicateIDException;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public interface ItemPrefixFixer {

    @Shadow @Final public static ObjectMap<String, Item> allItems = null;

    /**
     * @author Mr_Zombii
     * @reason Fix Non-prefixed items
     */
    @Inject(method = "registerItem", at = @At("HEAD"))
    private static void registerItem(Item item, CallbackInfo ci) {
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
        ci.cancel();
    }

}
