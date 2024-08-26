package com.github.puzzle.game.engine.blocks;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.github.puzzle.game.mixins.accessors.ChunkShaderAccessor;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJsonTexture;
import finalforeach.cosmicreach.rendering.shaders.ChunkShader;

import java.util.HashMap;

public class CustomTextureLoader {

    /**
     * Updates the block texture atlas and puts the texture in the global
     * texture cache, so the regular ChunkShader methods don't try to load
     * the texture
     * @param textureName name of the texture, these are global be warned
*    *                    about name collision
     * @param blockPix a pixmap representing your texture, this has to follow the guidelines
     *                 from data modding, width and height have to be equal
     */
    public static void registerTexture(String textureName, Pixmap blockPix) {
        if (blockPix.getWidth() != blockPix.getHeight()) {
            throw new RuntimeException("Width and height of " + textureName + " must be the same!");
        } else {
            int terrainPixCurX = ChunkShaderAccessor.getTerrainPixCurX();
            int terrainPixCurY = ChunkShaderAccessor.getTerrainPixCurY();
            Pixmap allBlocksPix = ChunkShaderAccessor.getAllBlocksPix();
            Texture chunkTerrainTex = ChunkShaderAccessor.getChunkTerrainTex();
            HashMap<String, BlockModelJsonTexture> storedTexs = ChunkShaderAccessor.getStoredTexs();

            float[] uv = new float[]{(float)(terrainPixCurX / blockPix.getWidth()), (float)(terrainPixCurY / blockPix.getHeight())};
            allBlocksPix.drawPixmap(blockPix, terrainPixCurX, terrainPixCurY);
            terrainPixCurX += blockPix.getWidth();
            if ((float)terrainPixCurX > (float)(allBlocksPix.getWidth() * 15) / 16.0F) {
                terrainPixCurX = 0;
                terrainPixCurY += blockPix.getHeight();
            }

            if (chunkTerrainTex != null) {
                chunkTerrainTex.dispose();
                chunkTerrainTex = null;
            }

            ChunkShaderAccessor.setTerrainPixCurX(terrainPixCurX);
            ChunkShaderAccessor.setTerrainPixCurY(terrainPixCurY);
            ChunkShaderAccessor.setChunkTerrainTex(chunkTerrainTex);

            BlockModelJsonTexture t = new BlockModelJsonTexture();
            t.fileName = textureName;
            t.uv = uv;
            storedTexs.put(textureName, t);
        }
    }

    public static void setNormal(Vector3 normal, float vertX, float vertY, float vertZ) {
        normal.x = Math.signum(vertX - 0.5F);
        normal.y = Math.signum(vertY - 0.5F);
        normal.z = Math.signum(vertZ - 0.5F);
    }

    public static int createUBOFloatsIdx(final float u, final float v, Vector3 normal) {
        for (int i = 0; i < ChunkShader.faceTexBufFloats.size; i += 5) {
            if (ChunkShader.faceTexBufFloats.get(i) == u && ChunkShader.faceTexBufFloats.get(i + 1) == v
                && ChunkShader.faceTexBufFloats.get(i + 2) == normal.x
                && ChunkShader.faceTexBufFloats.get(i + 3) == normal.y
                && ChunkShader.faceTexBufFloats.get(i + 4) == normal.z
            ) {
                return i / 5;
            }
        }
        final int fIdx = ChunkShader.faceTexBufFloats.size / 5;
        ChunkShader.faceTexBufFloats.add(u);
        ChunkShader.faceTexBufFloats.add(v);
        ChunkShader.faceTexBufFloats.add(normal.x);
        ChunkShader.faceTexBufFloats.add(normal.y);
        ChunkShader.faceTexBufFloats.add(normal.z);
        return fIdx;
    }

}