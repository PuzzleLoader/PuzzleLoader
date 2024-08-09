package com.github.puzzle.game.mixins.refactors.items;

import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.NullStick;
import com.github.puzzle.game.items.data.DataTag;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemCatalog;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.items.SlotContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemCatalog.class)
public class ItemCatalogMixin extends SlotContainer {

    public ItemCatalogMixin(int numSlots) {
        super(numSlots);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/items/ItemCatalog;addItemStack(Lfinalforeach/cosmicreach/items/ItemStack;)Z"))
    private boolean nuhUh(ItemCatalog instance, ItemStack itemStack) {
        return false;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(int numSlots, CallbackInfo ci) {
        for (Block block : Block.allBlocks) {
            for (BlockState state : block.blockStates.values()) {
                if (!state.hasEmptyModel() && !state.catalogHidden) {
                    this.addItemStack(new ItemStack(state.getItem(), 100));
                }
            }
        }

        for (Item vanillaItem : Item.allItems.values()) {
            if (vanillaItem instanceof IModItem item) {
                if (item.getTagManifest().hasTag(NullStick.IS_DEBUG_ATTRIBUTE)) {
                    DataTag<Boolean> tag = item.getTagManifest().getTag(NullStick.IS_DEBUG_ATTRIBUTE).getTagAsType(Boolean.class);
                    if (!tag.attribute.getValue()) {
                        this.addItemStack(item.getDefaultItemStack());
                    }
                } else this.addItemStack(item.getDefaultItemStack());
            }
        }
    }
}
