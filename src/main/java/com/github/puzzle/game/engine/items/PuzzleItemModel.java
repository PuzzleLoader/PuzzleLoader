package com.github.puzzle.game.engine.items;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.github.puzzle.core.resources.PuzzleGameAssetLoader;
import com.github.puzzle.game.engine.shaders.ItemShader;
import com.github.puzzle.game.items.IModItem;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemModel;
import finalforeach.cosmicreach.rendering.MeshData;
import finalforeach.cosmicreach.rendering.RenderOrder;
import finalforeach.cosmicreach.rendering.meshes.IGameMesh;
import finalforeach.cosmicreach.rendering.shaders.GameShader;

import java.lang.ref.WeakReference;
import java.util.*;

public class PuzzleItemModel extends ItemModel {
    IGameMesh mesh;
    Texture texture = null;
    Pixmap pm;
    GameShader shader;

    public ModelBatch modelBatch = new ModelBatch();

    public PuzzleItemModel(IModItem item){
        MeshData meshData = new MeshData(ItemShader.DEFAULT_ITEM_SHADER, RenderOrder.FULLY_TRANSPARENT);
        shader = meshData.shader;
        texture = PuzzleGameAssetLoader.LOADER.getResource(item.getTexturePath(), Texture.class);
        texture.getTextureData().prepare();
        pm = texture.getTextureData().consumePixmap();
        itemMeshes = item.getMesh();
        if (itemMeshes == null)
            if (item.getID().equals("puzzle-loader:checker_board")) {
                buildMesh();
            } else buildOldMesh();
    }

    public Material makeMaterial() {
        Attribute attribute1 = TextureAttribute.createDiffuse(texture);
        return new Material(attribute1);
    }

    Array<Mesh> itemMeshes;

    public void buildOldMesh() {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();

        MeshPartBuilder meshBuilder = modelBuilder.part(
                "MOD_ITEM",
                GL20.GL_TRIANGLES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates,
                makeMaterial()
        );
//        meshBuilder.rect();
        meshBuilder.box(0.5f,0.5f,0.5f,1,1,.2f);
        meshBuilder.setUVRange(0, 0, 16, 16);
        itemMeshes = modelBuilder.end().meshes;
    }

    public void buildMesh() {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();

        MeshPartBuilder builder = modelBuilder.part(
                "GENERATED_ITEM", GL20.GL_TRIANGLES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates,
                new Material(TextureAttribute.createDiffuse(texture))
        );

        buildExpandingMesh(builder);

        itemMeshes = modelBuilder.end().meshes;
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

    public void buildExpandingMesh(MeshPartBuilder builder) {
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

        for (Integer x : rectMap.keySet()) {
            List<Rect> rects = rectMap.get(x);
            for (Rect rect : rects) {
                // -(rect.length - (rect.end + (rect.length / 2f) - 1)) / 2
                builder.box(
                        rect.x, -(rect.length - (rect.end + (rect.length / 2f) - 1)) / 2, 0, // pos
                        1, rect.length / 2f, 0 // size
                );
            }
        }
    }

    @Override
    public void render(Camera camera, Matrix4 modelMat) {
        this.shader.bind(camera);
        this.shader.bindOptionalMatrix4("u_projViewTrans", camera.combined);
        this.shader.bindOptionalMatrix4("u_modelMat", modelMat);
        this.shader.bindOptionalTexture("texDiffuse", TextureFaker.fakerColor(16, 16, Color.CORAL), 0);
        for (Mesh itemMesh : itemMeshes) {
            itemMesh.render(shader.shader, GL20.GL_TRIANGLES);
        }
        this.shader.unbind();
    }

    @Override
    public void dispose(WeakReference<Item> weakReference) {
        for (Mesh itemMesh : itemMeshes) {
            itemMesh.dispose();
        }
    }



}
