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
import finalforeach.cosmicreach.rendering.SharedQuadIndexData;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJson;
import finalforeach.cosmicreach.rendering.meshes.IGameMesh;
import finalforeach.cosmicreach.rendering.shaders.GameShader;

import java.lang.ref.WeakReference;

public class PuzzleItemModel extends ItemModel {
    IGameMesh mesh;
    public static Texture texture = null;
    Pixmap pm;
    GameShader shader;

    public ModelBatch modelBatch = new ModelBatch();

    public PuzzleItemModel(IModItem item){
        MeshData meshData = new MeshData(ItemShader.DEFAULT_ITEM_SHADER, RenderOrder.FULLY_TRANSPARENT);
        shader = meshData.shader;
        if (BlockModelJson.useIndices) {
            this.mesh = meshData.toIntIndexedMesh(true);
        } else {
            this.mesh = meshData.toSharedIndexMesh(true);
            if (this.mesh != null) {
                int numIndices = this.mesh.getNumVertices() * 6 / 4;
                SharedQuadIndexData.allowForNumIndices(numIndices, false);
            }
        }
        texture = PuzzleGameAssetLoader.LOADER.getResource(item.getTexturePath(), Texture.class);
        texture.getTextureData().prepare();
        pm = texture.getTextureData().consumePixmap();
        buildModel();
    }

    public Material makeMaterial() {
//        Attribute attribute = ColorAttribute.createDiffuse(Color.ORANGE);
        Attribute attribute1 = TextureAttribute.createDiffuse(texture);
        return new Material(attribute1);
    }

    Array<Mesh> itemMeshes;

    public void buildModel() {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();

        MeshPartBuilder meshBuilder = modelBuilder.part(
                "MOD_ITEM",
                GL20.GL_TRIANGLES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates,
                makeMaterial()
        );
        meshBuilder.box(0.5f,0.5f,0.5f,1,1,.2f);
        meshBuilder.setUVRange(0, 0, 16, 16);
        itemMeshes = modelBuilder.end().meshes;
    }

    @Override
    public void render(Camera camera, Matrix4 modelMat) {
        this.shader.bind(camera);
        this.shader.bindOptionalMatrix4("u_projViewTrans", camera.combined);
        this.shader.bindOptionalMatrix4("u_modelMat", modelMat);
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
