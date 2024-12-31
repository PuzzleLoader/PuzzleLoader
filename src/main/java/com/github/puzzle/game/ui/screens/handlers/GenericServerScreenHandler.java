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

    /**
     * This allows puzzle server to send Network Screen packet on behalf to the client.
     * <p>
     * You will need to handle GenericClientScreenHandler in ClientModInitializer to allow client to know what screen to open!
     * @see com.github.puzzle.game.ui.screens.handlers.GenericClientScreenHandler
     * @param blockEntityId blockEntity Identifier
     */
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
