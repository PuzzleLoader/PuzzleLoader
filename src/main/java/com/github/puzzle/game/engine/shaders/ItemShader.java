package com.github.puzzle.game.engine.shaders;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.github.puzzle.core.Identifier;
import com.github.puzzle.core.Puzzle;
import com.github.puzzle.core.resources.ResourceLocation;
import finalforeach.cosmicreach.rendering.shaders.GameShader;
import finalforeach.cosmicreach.world.Sky;

import java.lang.reflect.Field;

public class ItemShader extends GameShader {

    public static ItemShader DEFAULT_ITEM_SHADER;
    private static VertexAttribute posAttrib = VertexAttribute.Position();
    private static VertexAttribute lightingAttrib = new VertexAttribute(4, 4, "a_lighting");


    public ItemShader(String vertexShader, String fragmentShader) {
        super(vertexShader, fragmentShader);
        VertexAttribute[] allVertexAttributes = new VertexAttribute[]{posAttrib, lightingAttrib};
        this.allVertexAttributesObj = new VertexAttributes(allVertexAttributes);

        try {
            int count = 0;

            for(int i = 0; i < allVertexAttributes.length; ++i) {
                VertexAttribute attribute = allVertexAttributes[i];
                attribute.offset = count;
                switch (attribute.type) {
                    case 5124:
                    case 5125:
                        count += 4 * attribute.numComponents;
                        break;
                    default:
                        count += attribute.getSizeInBytes();
                }
            }

            Field vertexSizeField = VertexAttributes.class.getField("vertexSize");
            vertexSizeField.setAccessible(true);
            vertexSizeField.set(this.allVertexAttributesObj, count);
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var7) {
            var7.printStackTrace();
        }

    }

    public static void initItemShader() {
        DEFAULT_ITEM_SHADER = new ItemShader(
                new ResourceLocation(Puzzle.MOD_ID,"item_shader.vert.glsl").toString(),
                new ResourceLocation(Puzzle.MOD_ID,"item_shader.frag.glsl").toString());
    }

    public void bind(Camera worldCamera) {
        super.bind(worldCamera);
        this.shader.setUniformMatrix("u_projViewTrans", worldCamera.combined);
//        int texNum = 0;
//        this.bindOptionalTexture("texDiffuse", PuzzleItemModel.texture, texNum);
        Sky sky = Sky.currentSky;
        this.bindOptionalUniform3f("skyAmbientColor", sky.currentAmbientColor);
        this.bindOptionalUniform3f("skyColor", sky.currentSkyColor);
    }
}
