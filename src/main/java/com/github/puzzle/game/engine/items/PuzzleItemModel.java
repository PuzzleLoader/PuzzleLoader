package com.github.puzzle.game.engine.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.github.puzzle.core.resources.PuzzleGameAssetLoader;
import com.github.puzzle.game.engine.shaders.ItemShader;
import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.data.DataTagManifest;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.rendering.MeshData;
import finalforeach.cosmicreach.rendering.RenderOrder;
import finalforeach.cosmicreach.rendering.items.ItemModel;
import finalforeach.cosmicreach.rendering.shaders.GameShader;
import finalforeach.cosmicreach.world.Sky;
import finalforeach.cosmicreach.world.Zone;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PuzzleItemModel extends ItemModel {
    Texture texture;
    Pixmap pm;
    GameShader shader;

    IModItem item;
    static Matrix4 noRot = new Matrix4();

    String itemModelId;

    public PuzzleItemModel(IModItem item){
        noRot.setTranslation(0, -1f, 0);
        this.item = item;
        MeshData meshData = new MeshData(ItemShader.DEFAULT_ITEM_SHADER, RenderOrder.FULLY_TRANSPARENT);
        shader = meshData.shader;
        DataTagManifest manifest = item.getTagManifest();
        Texture localTex = PuzzleGameAssetLoader.LOADER.getResource(manifest.getTag(IModItem.TEXTURE_LOCATION_PRESET).getValue(), Texture.class);
        itemModelId = manifest.getTag(IModItem.MODEL_ID_PRESET).getValue().toString();

        if (localTex.getWidth() != localTex.getHeight()) throw new RuntimeException("TEXTURE MUST HAVE WIDTH AND HEIGHT Culprit Item: " + item.getID());

        Pixmap newPixmap = getPixmap(localTex);

        texture = new Texture(newPixmap);
        pm = newPixmap;

        PuzzleItemRendererConstants.initCamera();

        if (itemModelId.equals(IModItem.MODEL_2D_ITEM.toString()))
            buildMesh2D();
        else if (itemModelId.equals(IModItem.MODEL_2D_ITEM.toString()))
            buildMesh2_5D();
        else if (itemModelId.equals(IModItem.MODEL_USE_CUSTOM_MODEL.toString()))
            itemMeshes = item.getMesh();
    }

    private static @NotNull Pixmap getPixmap(Texture localTex) {
        TextureData data = localTex.getTextureData();
        data.prepare();

        Pixmap donorPixmap = data.consumePixmap();
        Pixmap newPixmap = new Pixmap(donorPixmap.getWidth(), donorPixmap.getHeight(), donorPixmap.getFormat());

        for (int x = 0; x < donorPixmap.getWidth(); x++) {
            for (int y = 0; y < donorPixmap.getHeight(); y++) {
                newPixmap.drawPixel(x, y, donorPixmap.getPixel((donorPixmap.getWidth() - 1) - x, (donorPixmap.getHeight() - 1) - y));
            }
        }
        return newPixmap;
    }

    Array<Mesh> itemMeshes;

    public void buildMesh2D() {
        MeshBuilder builder = new MeshBuilder();
        builder.begin(ItemShader.DEFAULT_ITEM_SHADER.allVertexAttributesObj, GL20.GL_TRIANGLES);

        VertexInfo topLeft0 = new VertexInfo();
        topLeft0.setPos(new Vector3(-1, 2, 0));
        topLeft0.setUV(new Vector2(1, 1));

        VertexInfo topRight0 = new VertexInfo();
        topRight0.setPos(new Vector3(1, 2, 0));
        topRight0.setUV(new Vector2(0, 1));

        VertexInfo bottomLeft0 = new VertexInfo();
        bottomLeft0.setPos(new Vector3(-1, 0, 0));
        bottomLeft0.setUV(new Vector2(1, 0));

        VertexInfo bottomRight0 = new VertexInfo();
        bottomRight0.setPos(new Vector3(1, 0, 0));
        bottomRight0.setUV(new Vector2(0, 0));

        builder.rect(topLeft0, bottomLeft0, bottomRight0, topRight0);

        VertexInfo topLeft1 = new VertexInfo();
        topLeft1.setPos(new Vector3(-1, 2, 0));
        topLeft1.setUV(topLeft0.uv.cpy());

        VertexInfo topRight1 = new VertexInfo();
        topRight1.setPos(new Vector3(1, 2, 0));
        topRight1.setUV(topRight0.uv.cpy());

        VertexInfo bottomLeft1 = new VertexInfo();
        bottomLeft1.setPos(new Vector3(-1, 0, 0));
        bottomLeft1.setUV(bottomLeft0.uv.cpy());

        VertexInfo bottomRight1 = new VertexInfo();
        bottomRight1.setPos(new Vector3(1, 0, 0));
        bottomRight1.setUV(bottomRight0.uv.cpy());

        builder.rect(topLeft1, topRight1, bottomRight1, bottomLeft1);

        itemMeshes = new Array<>();
        itemMeshes.add(builder.end());
    }

    public void buildMesh2_5D() {
        MeshBuilder builder = new MeshBuilder();
        builder.begin(ItemShader.DEFAULT_ITEM_SHADER.allVertexAttributesObj, GL20.GL_TRIANGLES);

        float sideOffs = 2f * (1f / 16f);

        VertexInfo topLeft0 = new VertexInfo();
        topLeft0.setPos(new Vector3(-1, 2, sideOffs / 2f));
        topLeft0.setUV(new Vector2(1, 1));

        VertexInfo topRight0 = new VertexInfo();
        topRight0.setPos(new Vector3(1, 2, sideOffs / 2f));
        topRight0.setUV(new Vector2(0, 1));

        VertexInfo bottomLeft0 = new VertexInfo();
        bottomLeft0.setPos(new Vector3(-1, 0, sideOffs / 2f));
        bottomLeft0.setUV(new Vector2(1, 0));

        VertexInfo bottomRight0 = new VertexInfo();
        bottomRight0.setPos(new Vector3(1, 0, sideOffs / 2f));
        bottomRight0.setUV(new Vector2(0, 0));

        builder.rect(topLeft0, bottomLeft0, bottomRight0, topRight0);

        VertexInfo topLeft1 = new VertexInfo();
        topLeft1.setPos(new Vector3(-1, 2, -(sideOffs / 2f)));
        topLeft1.setUV(topLeft0.uv.cpy());

        VertexInfo topRight1 = new VertexInfo();
        topRight1.setPos(new Vector3(1, 2, -(sideOffs / 2f)));
        topRight1.setUV(topRight0.uv.cpy());

        VertexInfo bottomLeft1 = new VertexInfo();
        bottomLeft1.setPos(new Vector3(-1, 0, -(sideOffs / 2f)));
        bottomLeft1.setUV(bottomLeft0.uv.cpy());

        VertexInfo bottomRight1 = new VertexInfo();
        bottomRight1.setPos(new Vector3(1, 0, -(sideOffs / 2f)));
        bottomRight1.setUV(bottomRight0.uv.cpy());

        builder.rect(topLeft1, topRight1, bottomRight1, bottomLeft1);
        buildExpandingMesh(builder);

        itemMeshes = new Array<>();
        itemMeshes.add(builder.end());
    }

    static class Rect {

        int length, start, x, end;

        public Rect(int length, int col, int start, int end) {
            this.length = length;
            this.start = start;
            this.x = col;
            this.end = end;
        }

        @Override
        public String toString() {
            return "Rect{ length:" + length + ", x:" + x + ", start:" + start + ", end:" + end + "}";
        }
    }

    public void buildExpandingMesh(MeshBuilder builder) {
        if (!pm.getFormat().equals(Pixmap.Format.RGBA8888)) return;
        Map<Integer, List<Rect>> rectMap = new HashMap<>();
        for (int x = 0; x < texture.getWidth(); x++) {
            List<Rect> rects = new ArrayList<>();

            int length = 0;
            boolean lastPixelWasClear = true;
            for (int y = 0; y < texture.getHeight(); y++) {
                int alpha = (pm.getPixel(x, y) & 0x000000FF);
                if (alpha != 0) {
                    lastPixelWasClear = false;
                    length++;
                }

                if (alpha == 0) {
                    if (!lastPixelWasClear) {
                        Rect rect = new Rect(length, x, y - length, y);
                        System.out.println(rect);
                        rects.add(rect);
                        length = 0;
                    }
                    lastPixelWasClear = true;
                }

                if (y == texture.getHeight()-1) {
                    Rect rect = new Rect(length, x, y - length, y);
                    System.out.println(rect);
                    rects.add(rect);
                    length = 0;
                }
            }
            rectMap.put(x, rects);
        }

    }

    static final Color tintColor = new Color();
    static final BlockPosition tmpBlockPos = new BlockPosition(null, 0, 0, 0);

    public void render(Camera camera, Matrix4 matrix4, Vector3 entityPos, boolean isInSlot) {
        if (isInSlot) {
            tintColor.set(Color.WHITE);
        } else {
            Zone zone = InGame.getLocalPlayer().getZone(InGame.world);
            Entity.setLightingColor(zone, entityPos, Sky.currentSky.currentAmbientColor, tintColor, tmpBlockPos, tmpBlockPos);
        }

        this.shader.bind(camera);
        this.shader.bindOptionalMatrix4("u_projViewTrans", camera.combined);
        this.shader.bindOptionalMatrix4("u_modelMat", matrix4);
        this.shader.bindOptionalUniform4f("tintColor", tintColor);
        this.shader.bindOptionalTexture("texDiffuse", texture, 0);
        for (Mesh itemMesh : itemMeshes) {
            itemMesh.render(shader.shader, GL20.GL_TRIANGLES);
        }
        this.shader.unbind();
    }

    @Override
    public void render(Vector3 vector3, Camera camera, Matrix4 matrix4, boolean b) {
        render(camera, noRot, new Vector3(0, 0, 0), true);
    }

    @Override
    public void dispose(WeakReference<Item> weakReference) {
        for (Mesh itemMesh : itemMeshes) {
            itemMesh.dispose();
        }
    }

    @Override
    public Camera getItemSlotCamera() {
        return PuzzleItemRendererConstants.itemCam2;
    }
    static final PerspectiveCamera heldItemCamera = new PerspectiveCamera();

    @Override
    public void renderAsHeldItem(Vector3 vector3, Camera camera, float popUpTimer, float maxPopUpTimer, float swingTimer, float maxSwingTimer) {
        Matrix4 tmpHeldMat4 = new Matrix4();

        heldItemCamera.fieldOfView = 50.0F;
        heldItemCamera.viewportHeight = camera.viewportHeight;
        heldItemCamera.viewportWidth = camera.viewportWidth;
        heldItemCamera.near = camera.near;
        heldItemCamera.far = camera.far;
        heldItemCamera.update();
        tmpHeldMat4.idt();
        float swing;
        if (popUpTimer > 0.0F) {
            swing = (float)Math.pow(popUpTimer / maxPopUpTimer, 2.0);
            tmpHeldMat4.translate(0.0F, -1.0F * swing, 0.0F);
        }

        tmpHeldMat4.translate(1.65F, -1.25F, -2.5F);
        tmpHeldMat4.rotate(Vector3.Y, -75.0F);
        tmpHeldMat4.translate(-0.25F, -0.25F, -0.25F);
        if (swingTimer > 0.0F) {
            swing = swingTimer / maxSwingTimer;
            swing = 1.0F - (float)Math.pow((double)(swing - 0.5F), 2.0) / 0.25F;
            tmpHeldMat4.rotate(Vector3.Z, 90.0F * swing);
            float st = -swing;
            tmpHeldMat4.translate(st * 2.0F, st, 0.0F);
        }

        if (item.isTool()) {
            tmpHeldMat4.translate(.6f,0, 0);
            tmpHeldMat4.translate(0,-.2f, 0);
            tmpHeldMat4.rotate(new Vector3(0, 0, 1), 20);
            tmpHeldMat4.rotate(new Vector3(1, 0, 0), 15);
        }

        Gdx.gl.glDisable(2929);
        this.render(heldItemCamera, tmpHeldMat4, vector3, false);
        Gdx.gl.glEnable(2929);
    }

    @Override
    public void renderAsItemEntity(Vector3 vector3, Camera camera, Matrix4 matrix4) {
        matrix4.translate(0.5F, 0.5F, 0.5F);
        render(camera, matrix4, vector3, false);
    }


}
