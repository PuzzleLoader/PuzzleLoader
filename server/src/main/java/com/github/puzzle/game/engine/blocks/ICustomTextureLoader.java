package com.github.puzzle.game.engine.blocks;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector3;
import com.github.puzzle.core.Constants;
import com.github.puzzle.core.loader.meta.Environment;
import com.github.puzzle.core.loader.util.Reflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ICustomTextureLoader {

    Logger LOGGER = LoggerFactory.getLogger("Puzzle | CustomTextureLoader");

    ICustomTextureLoader INSTANCE = Constants.SIDE == Environment.CLIENT ? (ICustomTextureLoader) Reflection.newInstance("com.github.puzzle.game.engine.blocks.CustomTextureLoader") : new ICustomTextureLoader() {
        @Override
        public int createUBOFloatsIdx(float u, float v, Vector3 vecNormal, Vector3 faceNormal) {
            LOGGER.error("CustomTextureLoader.createUBOFloatsIdx() is being called on ServerSide!");
            return 0;
        }

        @Override
        public void registerTexture(String textureName, Pixmap texture) {
            LOGGER.error("CustomTextureLoader.registerTexture is being called on ServerSide!");
        }
    };

    static void registerTex(String textureName, Pixmap texture) {
        INSTANCE.registerTexture(textureName, texture);
    }

    Vector3 tmpVertPos = new Vector3();
    static void setNormal(Vector3 normal, float vertX, float vertY, float vertZ) {
        tmpVertPos.set(vertX, vertY, vertZ);
        normal.x = Math.signum(vertX - 0.5F);
        normal.y = Math.signum(vertY - 0.5F);
        normal.z = Math.signum(vertZ - 0.5F);
    }

    static int makeUBOFloatsIdx(float uA, float vA, Vector3 tmpNormal, Vector3 tmpFaceNormal) {
        return INSTANCE.createUBOFloatsIdx(uA, vA, tmpNormal, tmpFaceNormal);
    }

    int createUBOFloatsIdx(final float u, final float v, Vector3 vecNormal, Vector3 faceNormal);
    void registerTexture(String textureName, Pixmap texture);

}
