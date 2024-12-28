package com.github.puzzle.game.mixins.client.items.rendering;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.puzzle.game.engine.items.model.ItemModelWrapper;
import com.llamalad7.mixinextras.sugar.Local;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.rendering.items.ItemModel;
import finalforeach.cosmicreach.rendering.items.ItemRenderer;
import finalforeach.cosmicreach.ui.widgets.ItemSlotWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemSlotWidget.class)
public class ItemSlotWidgetMixin {

    @Unique
    private static final Matrix4 identMat4 = new Matrix4();

    @Inject(method = "drawItem", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/rendering/items/ItemRenderer;drawItem(Lcom/badlogic/gdx/graphics/Camera;Lfinalforeach/cosmicreach/items/Item;)V", shift = At.Shift.BEFORE), cancellable = true)
    private void drawItem(Viewport itemViewport, CallbackInfo ci, @Local ItemStack itemStack, @Local Camera itemCam) {
        ItemModel model = ItemRenderer.getModel(itemStack.getItem(), true);
        if (model instanceof ItemModelWrapper itemModel) {
            itemModel.renderInSlot(null, itemStack, itemCam, identMat4, false);
            ci.cancel();
            return;
        }
        model.render(null, itemCam, identMat4, false, false);
        ci.cancel();
    }

}
