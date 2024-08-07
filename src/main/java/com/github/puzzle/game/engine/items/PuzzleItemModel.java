package com.github.puzzle.game.engine.items;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
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
import com.github.puzzle.game.util.BlockUtil;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemModel;
import finalforeach.cosmicreach.rendering.MeshData;
import finalforeach.cosmicreach.rendering.RenderOrder;
import finalforeach.cosmicreach.rendering.shaders.GameShader;
import finalforeach.cosmicreach.savelib.lightdata.blocklight.IBlockLightData;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Sky;
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

    public ModelBatch modelBatch = new ModelBatch();
    IModItem item;

    public PuzzleItemModel(IModItem item){
        this.item = item;
        MeshData meshData = new MeshData(ItemShader.DEFAULT_ITEM_SHADER, RenderOrder.FULLY_TRANSPARENT);
        shader = meshData.shader;
        Texture localTex = PuzzleGameAssetLoader.LOADER.getResource(item.getTexturePath(), Texture.class);
        Pixmap newPixmap = getPixmap(localTex);

        texture = new Texture(newPixmap);
        pm = newPixmap;

        itemMeshes = item.getMesh();
        if (itemMeshes == null)
            buildMesh();
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

    public void buildMesh() {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();

        MeshPartBuilder builder = modelBuilder.part(
                "GENERATED_ITEM", GL20.GL_TRIANGLES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates,
                new Material(TextureAttribute.createDiffuse(texture))
        );

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
        Player player = InGame.getLocalPlayer();
        Vector3 playerPos = player.getPosition();

        Chunk chunk = BlockUtil.getChunkAtVec(player.getZone(InGame.world), playerPos);
        BlockPosition pos = BlockUtil.getBlockPosAtVec(player.getZone(InGame.world), playerPos);

        IBlockLightData data = chunk.blockLightData;
        if (data != null) {
            short blockLight = data.getBlockLight(
                    pos.localX(),
                    pos.localY(),
                    pos.localZ()
            );
            int red = blockLight >> 8;
            int green = (blockLight - (red << 8)) >> 4;
            int blue = ((blockLight - (red << 8)) - (green << 4));
            if (camera != PuzzleItemRendererConstants.itemCam2) {
                this.shader.bindOptionalUniform4f("b_lighting", new Color(
                        red, green, blue, 255
                ));
                Sky sky = Sky.currentSky;
                this.shader.bindOptionalUniform3f("skyAmbientColor", sky.currentAmbientColor);
            } else {
                this.shader.bindOptionalUniform4f("b_lighting", new Color(
                        0, 0, 0, 255
                ));
            }
        } else {
            this.shader.bindOptionalUniform4f("b_lighting", new Color(
                    1, 1, 1, 255
            ));
        }
        this.shader.bindOptionalTexture("texDiffuse", texture, 0);
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
