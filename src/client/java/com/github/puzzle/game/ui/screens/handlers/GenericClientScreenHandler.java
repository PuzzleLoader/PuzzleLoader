package com.github.puzzle.game.ui.screens.handlers;

import com.github.puzzle.core.Constants;
import com.github.puzzle.core.loader.meta.EnvType;
import com.github.puzzle.game.ui.screens.BasePuzzleScreen;
import finalforeach.cosmicreach.BlockEntityScreenInfo;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.Threads;
import finalforeach.cosmicreach.items.containers.SlotContainer;
import finalforeach.cosmicreach.items.screens.BaseItemScreen;
import finalforeach.cosmicreach.ui.UI;
import finalforeach.cosmicreach.util.Identifier;

import java.util.function.Consumer;
import java.util.function.Function;

public class GenericClientScreenHandler implements Consumer<BlockEntityScreenInfo> {

    public static void register(Identifier blockEntityId, Function<BlockEntityScreenInfo, BaseItemScreen> screenSupplier) {
            GameSingletons.registerBlockEntityScreenOpener(blockEntityId.toString(), Constants.SIDE == EnvType.SERVER ? new GenericServerScreenHandler() : new GenericClientScreenHandler(new SlotContainer(0), screenSupplier));
    }

    public static void register(Identifier blockEntityId, SlotContainer container, Function<BlockEntityScreenInfo, BaseItemScreen> screenSupplier) {
            GameSingletons.registerBlockEntityScreenOpener(blockEntityId.toString(), Constants.SIDE == EnvType.SERVER ? new GenericServerScreenHandler() : new GenericClientScreenHandler(container, screenSupplier));
    }

    SlotContainer container;
    Function<BlockEntityScreenInfo, BaseItemScreen> screenSupplier;

    public GenericClientScreenHandler(SlotContainer container, Function<BlockEntityScreenInfo, BaseItemScreen> screenSupplier) {
        this.container = container;
        this.screenSupplier = screenSupplier;
    }

    @Override
    public void accept(BlockEntityScreenInfo info) {
        Threads.runOnMainThread(() -> {
            BaseItemScreen screen = screenSupplier.apply(info);
            UI.addOpenBaseItemScreen(container, screen);
        });
    }
}
