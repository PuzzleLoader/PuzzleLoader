package dev.crmodders.puzzle.bugfixes;

import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemPickblock {

    @Shadow private Item item;

    @Inject(method = "setItem", at = @At("HEAD"), cancellable = true)
    private void setItem(Item item, CallbackInfo ci) {
        if (item == null) {
            item = Block.allBlocks.first().getDefaultBlockState().getItem();
            ci.cancel();
        }
    }

}
