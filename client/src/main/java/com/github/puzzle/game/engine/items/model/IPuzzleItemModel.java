package com.github.puzzle.game.engine.items.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.rendering.items.ItemModel;

import java.lang.ref.WeakReference;

public interface IPuzzleItemModel {

    default ItemModel wrap() {
        return new ItemModelWrapper(this);
    }

    void renderInSlot(Vector3 pos, ItemStack stack, Camera slotCamera, Matrix4 tmpMatrix, boolean useAmbientLighting);
    void renderAsHeldItem(Vector3 pos, ItemStack stack, Camera handCam, float popUpTimer, float maxPopUpTimer, float swingTimer, float maxSwingTimer);
    void renderAsEntity(Vector3 pos, ItemStack stack, Camera entityCam, Matrix4 tmpMatrix);

    void dispose(WeakReference<Item> itemRef);
    Camera getItemSlotCamera();

}
