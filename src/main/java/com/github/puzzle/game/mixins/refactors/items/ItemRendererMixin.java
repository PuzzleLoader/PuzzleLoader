package com.github.puzzle.game.mixins.refactors.items;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.github.puzzle.game.engine.items.PuzzleItemRendererConstants;
import com.github.puzzle.game.items.IModItem;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.rendering.items.ItemModel;
import finalforeach.cosmicreach.rendering.items.ItemRenderer;
import finalforeach.cosmicreach.ui.UI;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow @Final private static Matrix4 identMat4;

    @Shadow
    public static <T extends Item> ItemModel getModel(T item, boolean createIfNull) {
        return null;
    }

//    @Shadow @Final private static Matrix4 tmpHeldMat4;
    private static Matrix4 noRot;

    static {
        noRot = new Matrix4();
        noRot.setTranslation(0, -1f, 0);

        PuzzleItemRendererConstants.initCamera();
    }

//    /**
//     * @author Mr_Zombii
//     * @reason Make IModItems not rotate
//     */
//    @Overwrite
//    public static void drawItem(Camera itemCam, Item item) {
//        ItemModel model = getModel(item, true);
//        assert model != null;
//        if (item instanceof IModItem) {
//            UI.itemCursor.itemViewport.setCamera(PuzzleItemRendererConstants.itemCam2);
//            model.render(PuzzleItemRendererConstants.itemCam2, noRot);
//        } else {
//            model.render(itemCam, identMat4);
//        }
//    }

}
