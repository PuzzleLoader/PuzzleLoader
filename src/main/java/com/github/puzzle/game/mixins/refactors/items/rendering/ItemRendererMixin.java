package com.github.puzzle.game.mixins.refactors.items.rendering;

import com.github.puzzle.game.items.puzzle.ItemInstance;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.rendering.items.ItemModel;
import finalforeach.cosmicreach.rendering.items.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static finalforeach.cosmicreach.rendering.items.ItemRenderer.getModel;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Inject(method = "getModel", cancellable = true, at = @At("HEAD"))
    private static <T extends Item> void getModel0(T item, boolean createIfNull, CallbackInfoReturnable<ItemModel> cir) {
        if (item instanceof ItemInstance && ((ItemInstance) item).getParentItem() != null) {
            cir.setReturnValue(getModel(((ItemInstance) item).getParentItem(), createIfNull));
            return;
        }
    }
}
