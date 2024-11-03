package com.github.puzzle.game.ui.screens;

import finalforeach.cosmicreach.BlockEntityScreenInfo;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.items.ISlotContainerParent;
import finalforeach.cosmicreach.items.screens.BaseItemScreen;
import finalforeach.cosmicreach.util.Identifier;

public class BasePuzzleScreen extends BaseItemScreen {

    public BasePuzzleScreen(ISlotContainerParent parent) {
        super(parent);
    }

//    public static void registerScreen(Identifier identifier, BasePuzzleScreen screen){
//        GameSingletons.registerBlockEntityScreenOpener(identifier.toString(), screen::OnOpen);
//        GameSingletons.blockEntityScreenOpeners.put(identifier.toString(), screen::OnOpen);
//    }

    public void OnOpen(BlockEntityScreenInfo info) {}
}
