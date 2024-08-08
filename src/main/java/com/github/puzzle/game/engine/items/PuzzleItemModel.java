package com.github.puzzle.game.engine.items;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
        Texture localTex = PuzzleGameAssetLoader.LOADER.getResource(item.getTexturePath(), Texture.class);
        TextureData data = localTex.getTextureData();
        data.prepare();

        Pixmap donorPixmap = data.consumePixmap();
        Pixmap newPixmap = new Pixmap(donorPixmap.getWidth(), donorPixmap.getHeight(), donorPixmap.getFormat());

        for (int x = 0; x < donorPixmap.getWidth(); x++) {
            for (int y = 0; y < donorPixmap.getHeight(); y++) {
                newPixmap.drawPixel(x, y, donorPixmap.getPixel((donorPixmap.getWidth() - 1) - x, (donorPixmap.getHeight() - 1) - y));
            }
        }

        texture = new Texture(newPixmap);
        pm = newPixmap;


        itemMeshes = item.getMesh();
        if (itemMeshes == null)
            buildMesh();
//            if (item.getID().equals("puzzle-loader:checker_board")) {
//                buildMesh();
//            } else buildOldMesh();
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

        VertexInfo topLeft0 = new VertexInfo();
        topLeft0.setPos(new Vector3(-1, 1, 0));
        topLeft0.setUV(new Vector2(1, 1));

        VertexInfo topRight0 = new VertexInfo();
        topRight0.setPos(new Vector3(1, 1, 0));
        topRight0.setUV(new Vector2(0, 1));

        VertexInfo bottomLeft0 = new VertexInfo();
        bottomLeft0.setPos(new Vector3(-1, -1, 0));
        bottomLeft0.setUV(new Vector2(1, 0));

        VertexInfo bottomRight0 = new VertexInfo();
        bottomRight0.setPos(new Vector3(1, -1, 0));
        bottomRight0.setUV(new Vector2(0, 0));

        builder.rect(topLeft0, bottomLeft0, bottomRight0, topRight0);
//        builder.rect(topLeft0, topRight0, bottomRight0, bottomLeft0);

        VertexInfo topLeft1 = new VertexInfo();
        topLeft1.setPos(new Vector3(-1, 1, -.5f));
        topLeft1.setUV(new Vector2(1, 1));

        VertexInfo topRight1 = new VertexInfo();
        topRight1.setPos(new Vector3(1, 1, -.5f));
        topRight1.setUV(new Vector2(0, 1));

        VertexInfo bottomLeft1 = new VertexInfo();
        bottomLeft1.setPos(new Vector3(-1, -1, -.5f));
        bottomLeft1.setUV(new Vector2(1, 0));

        VertexInfo bottomRight1 = new VertexInfo();
        bottomRight1.setPos(new Vector3(1, -1, -.5f));
        bottomRight1.setUV(new Vector2(0, 0));

//        builder.rect(topRight1, bottomRight1, bottomLeft1, topLeft1);
        builder.rect(topLeft1, topRight1, bottomRight1, bottomLeft1);
//        builder.rect(bottomLeft1, bottomRight1, topRight1, topLeft1);
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

    }

    @Override
    public void render(Camera camera, Matrix4 modelMat) {
        this.shader.bind(camera);
        this.shader.bindOptionalMatrix4("u_projViewTrans", camera.combined);
        this.shader.bindOptionalMatrix4("u_modelMat", modelMat);
        this.shader.bindOptionalTexture("texDiffuse", texture, 0);
//        this.shader.bindOptionalTexture("texDiffuse", TextureFaker.fakerColor(16, 16, Color.CORAL), 0);
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
