package com.github.puzzle.game.ui.screens.handlers;

import com.github.puzzle.core.Constants;
import com.github.puzzle.core.loader.meta.EnvType;
import finalforeach.cosmicreach.BlockEntityScreenInfo;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.blockentities.BlockEntity;
import finalforeach.cosmicreach.networking.packets.blocks.BlockEntityDataPacket;
import finalforeach.cosmicreach.networking.packets.blocks.BlockEntityScreenPacket;
import finalforeach.cosmicreach.networking.server.ServerIdentity;
import finalforeach.cosmicreach.networking.server.ServerSingletons;
import finalforeach.cosmicreach.util.Identifier;

import java.util.function.Consumer;

public class GenericServerScreenHandler implements Consumer<BlockEntityScreenInfo> {

    public static void register(Identifier blockEntityId) {
        if (Constants.SIDE == EnvType.CLIENT) throw new RuntimeException("Cannot Register Server-Sided Screen Handler On Client");
        GameSingletons.registerBlockEntityScreenOpener(blockEntityId.toString(), new GenericServerScreenHandler());
    }

    @Override
    public void accept(BlockEntityScreenInfo info) {
        ServerIdentity id = ServerSingletons.getConnection(info.player());
        BlockEntity blockEntity = info.blockEntity();
        id.send(new BlockEntityDataPacket(blockEntity));
        id.send(new BlockEntityScreenPacket(blockEntity));
    }

}
