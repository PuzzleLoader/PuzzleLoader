package com.github.puzzle.game.block.generators.model;

import com.github.puzzle.core.Constants;
import com.github.puzzle.core.loader.launch.Piece;
import com.github.puzzle.core.loader.meta.EnvType;
import com.github.puzzle.game.factories.IGenerator;
import finalforeach.cosmicreach.util.Identifier;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public interface IBlockModelGenerator extends IGenerator {

    static IBlockModelGenerator NEW(Identifier blockId, String model) {
        if (Constants.SIDE == EnvType.CLIENT) {
            Constructor<?> constructor;
            try {
                 constructor = Class.forName("com.github.puzzle.game.block.generators.model.BlockModelGenerator", false, Piece.classLoader).getConstructor(Identifier.class, String.class);
            } catch (NoSuchMethodException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            try {
                return (IBlockModelGenerator) constructor.newInstance(blockId, model);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    String getModelName();

    static String getModelName(Identifier blockId, String modelName) {
        return blockId.toString() + "_" + modelName;
    }

    void createTexture(String all, Identifier of);

    void createCuboid(int i, int i1, int i2, int i3, int i4, int i5, String all);
}
