package com.github.puzzle.game.mixins.refactors.items;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.github.puzzle.game.items.IModItem;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemModel;
import finalforeach.cosmicreach.rendering.items.ItemRenderer;
import finalforeach.cosmicreach.ui.UI;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow @Final private static Matrix4 identMat4;

    @Shadow
    public static <T extends Item> ItemModel getModel(T item, boolean createIfNull) {
        return null;
    }

    @Shadow @Final private static Matrix4 tmpHeldMat4;
    private static Matrix4 noRot;
    private static Camera itemCam2 ;

    static {
        noRot = new Matrix4();
        noRot.setTranslation(0, -1f, 0);

        itemCam2 = new OrthographicCamera(100.0F, 100.0F);
        itemCam2.position.set(0, 0, 2);
        itemCam2.lookAt(0, 0, 0);
        ((OrthographicCamera) itemCam2).zoom = 0.027F;
        itemCam2.update();
    }

    @Inject(method = "renderHeldItem(Lfinalforeach/cosmicreach/items/Item;Lcom/badlogic/gdx/graphics/PerspectiveCamera;)V", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/items/ItemModel;render(Lcom/badlogic/gdx/graphics/Camera;Lcom/badlogic/gdx/math/Matrix4;)V", shift = At.Shift.BEFORE))
    private static void itemChecker(Item heldItem, PerspectiveCamera worldCamera, CallbackInfo ci) {
        if (heldItem instanceof IModItem item) {
            if (item.isTool()) {
//                tmpHeldMat4.rotate(new Vector3(0, 1, 0), 180);
                tmpHeldMat4.translate(.6f,0, 0);
                tmpHeldMat4.translate(0,-.2f, 0);
                tmpHeldMat4.rotate(new Vector3(0, 0, 1), 20);
                tmpHeldMat4.rotate(new Vector3(1, 0, 0), 15);
            }
        }
    }

    /**
     * @author Mr_Zombii
     * @reason Make IModItems not rotate
     */
    @Overwrite
    public static void drawItem(Camera itemCam, Item item) {
        ItemModel model = getModel(item, true);
        assert model != null;
        if (item instanceof IModItem) {
            UI.itemCursor.itemViewport.setCamera(itemCam2);
            model.render(itemCam2, noRot);
        } else {
            model.render(itemCam, identMat4);
        }
    }

}
