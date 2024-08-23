package com.github.puzzle.game.engine.items;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.github.puzzle.game.items.puzzle.ItemInstance;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.rendering.items.ItemModel;
import finalforeach.cosmicreach.ui.UI;

import java.lang.ref.WeakReference;

public class InstanceModelWrapper extends ItemModel {

    ItemInstance instance;
    ItemModel model;

    public ItemInstance getInstance() {
        return instance;
    }

    public ItemModel getModel() {
        return model;
    }

    public InstanceModelWrapper(ItemInstance itemInstance, ItemModel model) {
        this.instance = itemInstance;
        if (model == null) {
            this.model = new ItemModel() {
                @Override
                public void render(Vector3 vector3, Camera camera, Matrix4 matrix4, boolean b) {

                }

                @Override
                public void dispose(WeakReference<Item> weakReference) {

                }

                Camera camera = new OrthographicCamera();

                @Override
                public Camera getItemSlotCamera() {
                    return camera;
                }

                @Override
                public void renderAsHeldItem(Vector3 vector3, Camera camera, float v, float v1, float v2, float v3) {

                }

                @Override
                public void renderAsItemEntity(Vector3 vector3, Camera camera, Matrix4 matrix4) {

                }
            };
        } else this.model = model;
    }

    @Override
    public void render(Vector3 vector3, Camera camera, Matrix4 matrix4, boolean b) {
        model.render(vector3, camera, matrix4, b);
    }

    @Override
    public void dispose(WeakReference<Item> weakReference) {
        model.dispose(weakReference);
    }

    @Override
    public Camera getItemSlotCamera() {
        return model.getItemSlotCamera();
    }

    @Override
    public void renderAsHeldItem(Vector3 vector3, Camera camera, float v, float v1, float v2, float v3) {
        model.renderAsHeldItem(vector3, camera, v, v1, v2, v3);
    }

    @Override
    public void renderAsItemEntity(Vector3 vector3, Camera camera, Matrix4 matrix4) {
        model.renderAsItemEntity(vector3, camera, matrix4);
    }
}
