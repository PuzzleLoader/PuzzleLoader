package com.github.puzzle.game.ui.screens.handlers;

import com.github.puzzle.core.Constants;
import com.github.puzzle.core.loader.meta.EnvType;
import finalforeach.cosmicreach.BlockEntityScreenInfo;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.blockentities.BlockEntity;
import finalforeach.cosmicreach.blockentities.BlockEntityItemContainer;
import finalforeach.cosmicreach.items.containers.SlotContainer;
import finalforeach.cosmicreach.items.screens.BaseItemScreen;
import finalforeach.cosmicreach.items.screens.ItemStorageScreen;
import finalforeach.cosmicreach.ui.UI;
import finalforeach.cosmicreach.util.Identifier;

import java.util.function.Consumer;
import java.util.function.Function;

public class GenericClientScreenHandler implements Consumer<BlockEntityScreenInfo> {

    public static void register(Identifier blockEntityId, BaseItemScreen screen) {
        GameSingletons.registerBlockEntityScreenOpener(
                blockEntityId.toString(),
                Constants.SIDE == EnvType.SERVER ? new GenericServerScreenHandler() : new GenericClientScreenHandler(null, (i) -> screen)
        );
    }

    SlotContainer container;
    Function<BlockEntityScreenInfo, BaseItemScreen> screenSupplier;

    public GenericClientScreenHandler(SlotContainer container, Function<BlockEntityScreenInfo, BaseItemScreen> screenSupplier) {
        this.container = container;
        this.screenSupplier = screenSupplier;
    }

    @Override
    public void accept(BlockEntityScreenInfo info) {
        UI.addOpenBaseItemScreen(container, screenSupplier.apply(info));
    }

}
