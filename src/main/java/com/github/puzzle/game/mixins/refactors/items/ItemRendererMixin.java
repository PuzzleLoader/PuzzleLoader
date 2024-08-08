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

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow @Final private static Matrix4 identMat4;

    @Shadow
    public static <T extends Item> ItemModel getModel(T item, boolean createIfNull) {
        return null;
    }

    private static Matrix4 noRot;
    private static Camera itemCam2 ;

    static {
        noRot = new Matrix4();

        itemCam2 = new OrthographicCamera(100.0F, 100.0F);
        itemCam2.position.set(0, 0, 2);
        itemCam2.lookAt(0, 0, 0);
        ((OrthographicCamera) itemCam2).zoom = 0.027F;
        itemCam2.update();
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
//            model.render(itemCam, identMat4);
            UI.itemCursor.itemViewport.setCamera(itemCam2);
            model.render(itemCam2, noRot);
        }
    }

}
