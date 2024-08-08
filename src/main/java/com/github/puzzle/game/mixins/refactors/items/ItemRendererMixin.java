package com.github.puzzle.game.mixins.refactors.items;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.github.puzzle.game.items.IModItem;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemModel;
import finalforeach.cosmicreach.items.ItemSlotWidget;
import finalforeach.cosmicreach.rendering.items.ItemRenderer;
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

    @Shadow @Final private static Matrix4 tmpHeldMat4;

    /**
     * @author Mr_Zombii
     * @reason Make IModItems not rotate
     */
    @Overwrite
    public static void drawItem(Camera itemCam, Item item) {
        ItemModel model = getModel(item, true);
        assert model != null;
        if (item instanceof IModItem) {
            model.render(itemCam, identMat4);
        } else {
            model.render(itemCam, identMat4);
        }
    }

}
