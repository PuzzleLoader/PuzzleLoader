package com.github.puzzle.game.ui.screens;

import com.github.puzzle.core.Identifier;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.blockentities.BlockEntity;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.items.screens.BaseItemScreen;
import finalforeach.cosmicreach.world.Zone;

public class BasePuzzleScreen extends BaseItemScreen {

    public static void registerScreen(Identifier identifier, BasePuzzleScreen screen){
        GameSingletons.blockEntityScreenOpeners.put(identifier.toString(), screen::OnOpen);
    }

    public void OnOpen(Player player, Zone zone, BlockEntity blockEntity) {
    }
}
