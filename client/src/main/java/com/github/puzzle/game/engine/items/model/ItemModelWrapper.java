package com.github.puzzle.game.engine.items.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.rendering.items.ItemModel;
import finalforeach.cosmicreach.ui.UI;

import java.lang.ref.WeakReference;

public class ItemModelWrapper extends ItemModel implements IPuzzleItemModel {

    IPuzzleItemModel parent;
    public ItemModelWrapper(IPuzzleItemModel parent) {
        this.parent = parent;
    }

    @Override
    public void renderInSlot(Vector3 pos, ItemStack stack, Camera slotCamera, Matrix4 tmpMatrix, boolean useAmbientLighting) {
        parent.renderInSlot(pos, stack, slotCamera, tmpMatrix, useAmbientLighting);
    }

    @Override
    public void render(Vector3 vector3, Camera camera, Matrix4 matrix4, boolean b) {
        renderInSlot(vector3, null, camera, matrix4, b);
    }

    @Override
    public void dispose(WeakReference<Item> itemRef) {
        parent.dispose(itemRef);
    }

    @Override
    public Camera getItemSlotCamera() {
        return parent.getItemSlotCamera();
    }

    @Override
    public void renderAsHeldItem(Vector3 vector3, Camera camera, float v, float v1, float v2, float v3) {
        ItemStack stack = UI.hotbar.getSelectedItemStack();
        renderAsHeldItem(vector3, stack, camera, v, v1, v2, v3);
    }

    @Override
    public void renderAsItemEntity(Vector3 vector3, Camera camera, Matrix4 matrix4) {
        renderAsEntity(vector3, null, camera, matrix4);
    }

    @Override
    public void renderAsHeldItem(Vector3 pos, ItemStack stack, Camera handCam, float popUpTimer, float maxPopUpTimer, float swingTimer, float maxSwingTimer) {
        parent.renderAsHeldItem(pos, stack, handCam, popUpTimer, maxPopUpTimer, swingTimer, maxSwingTimer);
    }

    @Override
    public void renderAsEntity(Vector3 pos, ItemStack stack, Camera entityCam, Matrix4 tmpMatrix) {
        parent.renderAsEntity(pos, stack, entityCam, tmpMatrix);
    }
}
