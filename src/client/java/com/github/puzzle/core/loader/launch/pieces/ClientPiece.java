package com.github.puzzle.core.loader.launch.pieces;

import com.github.puzzle.core.annotation.Internal;
import com.github.puzzle.core.loader.launch.Piece;
import com.github.puzzle.core.loader.meta.Env;
import com.github.puzzle.core.loader.meta.EnvType;

@Internal
@Env(EnvType.CLIENT)
public class ClientPiece {

    public static void main(String[] args) {
        Piece.launch(args, EnvType.CLIENT);
    }

}
