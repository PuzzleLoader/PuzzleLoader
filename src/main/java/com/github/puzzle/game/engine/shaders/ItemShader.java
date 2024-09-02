package com.github.puzzle.game.engine.shaders;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.github.puzzle.core.Puzzle;
import com.github.puzzle.core.resources.ResourceLocation;
import finalforeach.cosmicreach.rendering.shaders.GameShader;

public class ItemShader extends GameShader {

    public static ItemShader DEFAULT_ITEM_SHADER;
    private static final VertexAttribute posAttrib = VertexAttribute.Position();
    private static final VertexAttribute texCoordsAttrib = VertexAttribute.TexCoords(0);
    private static final VertexAttribute normalAttrib = VertexAttribute.Normal();


    public ItemShader(String vertexShader, String fragmentShader) {
        super(vertexShader, fragmentShader);
        this.allVertexAttributesObj = new VertexAttributes(posAttrib, texCoordsAttrib, normalAttrib);
    }

    public static void initItemShader() {
        DEFAULT_ITEM_SHADER = new ItemShader(
                new ResourceLocation(Puzzle.MOD_ID,"item_shader.vert.glsl").toString(),
                new ResourceLocation(Puzzle.MOD_ID,"item_shader.frag.glsl").toString());
    }

    public void bind(Camera worldCamera) {
        super.bind(worldCamera);
        this.shader.setUniformMatrix("u_projViewTrans", worldCamera.combined);
    }
}
