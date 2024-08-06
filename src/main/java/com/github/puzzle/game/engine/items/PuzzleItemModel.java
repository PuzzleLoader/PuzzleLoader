package com.github.puzzle.game.engine.items;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.github.puzzle.core.resources.PuzzleGameAssetLoader;
import com.github.puzzle.game.items.IModItem;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemModel;
import finalforeach.cosmicreach.rendering.MeshData;
import finalforeach.cosmicreach.rendering.RenderOrder;
import finalforeach.cosmicreach.rendering.SharedQuadIndexData;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJson;
import finalforeach.cosmicreach.rendering.meshes.IGameMesh;
import finalforeach.cosmicreach.rendering.shaders.ChunkShader;
import finalforeach.cosmicreach.rendering.shaders.GameShader;

import java.lang.ref.WeakReference;

public class PuzzleItemModel extends ItemModel {
    IGameMesh mesh;
    GameShader shader;
    Texture texture;
    Pixmap pm;

    public PuzzleItemModel(IModItem item){
        MeshData meshData = new MeshData(ChunkShader.DEFAULT_BLOCK_SHADER, RenderOrder.FULLY_TRANSPARENT);

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
    }


    @Override
    public void render(Camera camera, Matrix4 modelMat) {
        if (this.mesh != null) {
            if (!BlockModelJson.useIndices) {
                SharedQuadIndexData.bind();
            }
            this.shader.bindOptionalMatrix4("u_projViewTrans", camera.combined);
            this.shader.bindOptionalMatrix4("u_modelMat", modelMat);
            this.mesh.bind(this.shader.shader);
            this.mesh.render(this.shader.shader, 4);
            texture.draw(pm,0,0);
            this.mesh.unbind(this.shader.shader);
            this.shader.unbind();
            if (!BlockModelJson.useIndices) {
                SharedQuadIndexData.unbind();
            }
        }
    }

    @Override
    public void dispose(WeakReference<Item> weakReference) {
       mesh.dispose();
    }



}
