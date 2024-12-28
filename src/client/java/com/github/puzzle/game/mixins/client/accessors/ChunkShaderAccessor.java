package com.github.puzzle.game.mixins.client.accessors;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJsonTexture;
import finalforeach.cosmicreach.rendering.shaders.ChunkShader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.HashMap;

@Mixin(ChunkShader.class)
public interface ChunkShaderAccessor {

    @Accessor static int getTerrainPixCurX() { return 0; }
    @Accessor static void setTerrainPixCurX(int x) { }
    @Accessor static int getTerrainPixCurY() { return 0; }
    @Accessor static void setTerrainPixCurY(int y) { }
    @Accessor static Pixmap getAllBlocksPix() { return null; }
    @Accessor static Texture getChunkTerrainTex() { return null; }
    @Accessor static void setChunkTerrainTex(Texture tex) { }
    @Accessor static HashMap<String, BlockModelJsonTexture> getStoredTexs() { return null; }
}