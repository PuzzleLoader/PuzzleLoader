package com.github.puzzle.game.generators;

import com.badlogic.gdx.graphics.Pixmap;
import finalforeach.cosmicreach.util.Identifier;

public class BasicCubeModelGenerator extends BlockModelGenerator {

    public BasicCubeModelGenerator(Identifier blockId, String modelName, boolean ambientOcclusion, Pixmap side, Pixmap front) {
        super(blockId, modelName);
        createTexture("side", side);
        createTexture("front", front);
        createCuboid(0, 0, 0, 16, 16, 16, "side", "side", "side", "front").setAmbientOcclusion(ambientOcclusion);
    }

    public BasicCubeModelGenerator(Identifier blockId, String modelName, boolean ambientOcclusion, Identifier side, Identifier front) {
        super(blockId, modelName);
        createTexture("side", side);
        createTexture("front", front);
        createCuboid(0, 0, 0, 16, 16, 16, "side", "side", "side", "front").setAmbientOcclusion(ambientOcclusion);
    }

    public BasicCubeModelGenerator(Identifier blockId, String modelName, boolean ambientOcclusion, Pixmap top, Pixmap bottom, Pixmap side) {
        super(blockId, modelName);
        createTexture("top", top);
        createTexture("bottom", bottom);
        createTexture("side", side);
        createCuboid(0, 0, 0, 16, 16, 16, "top", "bottom", "side").setAmbientOcclusion(ambientOcclusion);
    }

    public BasicCubeModelGenerator(Identifier blockId, String modelName, boolean ambientOcclusion, Identifier top, Identifier bottom, Identifier side) {
        super(blockId, modelName);
        createTexture("top", top);
        createTexture("bottom", bottom);
        createTexture("side", side);
        createCuboid(0, 0, 0, 16, 16, 16, "top", "bottom", "side").setAmbientOcclusion(ambientOcclusion);
    }

    public BasicCubeModelGenerator(Identifier blockId, String modelName, boolean ambientOcclusion, Pixmap top, Pixmap bottom, Pixmap side, Pixmap front) {
        super(blockId, modelName);
        createTexture("top", top);
        createTexture("bottom", bottom);
        createTexture("side", side);
        createTexture("front", front);
        createCuboid(0, 0, 0, 16, 16, 16, "top", "bottom", "side", "front").setAmbientOcclusion(ambientOcclusion);
    }

    public BasicCubeModelGenerator(Identifier blockId, String modelName, boolean ambientOcclusion, Identifier top, Identifier bottom, Identifier side, Identifier front) {
        super(blockId, modelName);
        createTexture("top", top);
        createTexture("bottom", bottom);
        createTexture("side", side);
        createTexture("front", front);
        createCuboid(0, 0, 0, 16, 16, 16, "top", "bottom", "side", "front").setAmbientOcclusion(ambientOcclusion);
    }

    public BasicCubeModelGenerator(Identifier blockId, String modelName, boolean ambientOcclusion, Pixmap top, Pixmap bottom, Pixmap left, Pixmap right, Pixmap front, Pixmap back) {
        super(blockId, modelName);
        createTexture("top", top);
        createTexture("bottom", bottom);
        createTexture("left", left);
        createTexture("right", right);
        createTexture("front", front);
        createTexture("back", back);
        createCuboid(0, 0, 0, 16, 16, 16, "top", "bottom", "left", "right", "front", "back").setAmbientOcclusion(ambientOcclusion);
    }

    public BasicCubeModelGenerator(Identifier blockId, String modelName, boolean ambientOcclusion, Identifier top, Identifier bottom, Identifier left, Identifier right, Identifier front, Identifier back) {
        super(blockId, modelName);
        createTexture("top", top);
        createTexture("bottom", bottom);
        createTexture("left", left);
        createTexture("right", right);
        createTexture("front", front);
        createTexture("back", back);
        createCuboid(0, 0, 0, 16, 16, 16, "top", "bottom", "left", "right", "front", "back").setAmbientOcclusion(ambientOcclusion);
    }

}
