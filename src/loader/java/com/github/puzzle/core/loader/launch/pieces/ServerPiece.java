package com.github.puzzle.core.loader.launch.pieces;

import com.github.puzzle.core.loader.launch.Piece;
import com.github.puzzle.core.loader.meta.EnvType;

public class ServerPiece {

    public static void main(String[] args) {
        Piece.launch(args, EnvType.SERVER);
    }

}
