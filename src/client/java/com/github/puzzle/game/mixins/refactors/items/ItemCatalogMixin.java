package com.github.puzzle.game.mixins.refactors.items;

import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.puzzle.NullStick;
import finalforeach.cosmicreach.items.ItemCatalog;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.items.containers.SlotContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemCatalog.class)
public class ItemCatalogMixin extends SlotContainer {

    public ItemCatalogMixin(int numSlots) {
        super(numSlots);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/items/ItemCatalog;addItemStack(Lfinalforeach/cosmicreach/items/ItemStack;)Z"))
    private boolean nuhUh(ItemCatalog instance, ItemStack itemStack) {
        if (itemStack.getItem() instanceof IModItem item) {
            if (!item.isDebug()) addItemStack(item.getDefaultItemStack());
        } else addItemStack(itemStack);
        return false;
    }

}
