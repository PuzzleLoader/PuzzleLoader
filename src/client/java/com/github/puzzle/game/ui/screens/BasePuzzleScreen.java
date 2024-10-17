package com.github.puzzle.game.ui.screens;

import finalforeach.cosmicreach.BlockEntityScreenInfo;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.blockentities.BlockEntity;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.items.ISlotContainerParent;
import finalforeach.cosmicreach.items.screens.BaseItemScreen;
import finalforeach.cosmicreach.util.Identifier;
import finalforeach.cosmicreach.world.Zone;

public class BasePuzzleScreen extends BaseItemScreen {

    public BasePuzzleScreen(ISlotContainerParent parent) {
        super(parent);
    }

    public static void registerScreen(Identifier identifier, BasePuzzleScreen screen){
        GameSingletons.blockEntityScreenOpeners.put(identifier.toString(), screen::OnOpen);
    }

    public void OnOpen(BlockEntityScreenInfo info) {}
}
