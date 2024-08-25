package com.github.puzzle.game.worldgen;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import finalforeach.cosmicreach.blocks.BlockState;

import java.util.Arrays;

public class Schematic {
    Array<BlockState> blockStates;
    int[][][] schem;
    public int length, height, width;

    public Schematic(int size) {
        new Schematic(size, size, size);
    }

    public Schematic(Array<BlockState> blockStates, int size) {
        new Schematic(blockStates, size, size, size);
    }

    public Schematic(int length, int height, int width){
        Array<BlockState> defaultblockStates = new Array<>();
        defaultblockStates.add(null);
        new Schematic(defaultblockStates, length, height, width);
    }

    public Schematic(Array<BlockState> blockStates, int length, int height, int width) {
        int[][][] preschem = new int[length][height][width];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < width; k++) {
                    preschem[i][j][k] = 0;
                }
            }
        }
        new Schematic(blockStates, preschem, length, height, width);
    }

    public Schematic(Array<BlockState> blockStates, int[][][] schem, int length, int height, int width){
        this.length = length;
        this.height = height;
        this.width = width;
        this.blockStates = blockStates;
        this.schem = schem;
    }

    public Schematic(Pair<Array<BlockState>, int[][][]> data, int length, int height, int width){
//        length = (int)Math.floor(Math.abs(pos2.x - pos1.x));  // Length along x-axis
//        height = (int)Math.floor(Math.abs(pos2.y - pos1.y));  // Height along y-axis
//        width = (int)Math.floor(Math.abs(pos2.z - pos1.z));
        this.length = length;
        this.height = height;
        this.width = width;
        this.blockStates = data.getLeft();
        this.schem = data.getRight();
    }

    public BlockState getBlockStates(int x, int y, int z){
        return this.blockStates.get(this.schem[x][y][z]);
    }
}
