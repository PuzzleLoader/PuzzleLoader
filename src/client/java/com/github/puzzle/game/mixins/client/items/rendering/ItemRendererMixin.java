package com.github.puzzle.game.mixins.client.items.rendering;

import com.github.puzzle.game.engine.items.ItemThingModel;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemThing;
import finalforeach.cosmicreach.rendering.items.ItemModel;
import finalforeach.cosmicreach.rendering.items.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.ref.WeakReference;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Inject(method = "lambda$static$1", at = @At("HEAD"), cancellable = true)
    private static void replaceItem2D(WeakReference<Item> itemRef, CallbackInfoReturnable<ItemModel> cir) {
        cir.setReturnValue(new ItemThingModel((ItemThing) itemRef.get()).wrap());
//        cir.setReturnValue(new ItemModel2D((ItemThing) itemRef.get()));
    }

}
