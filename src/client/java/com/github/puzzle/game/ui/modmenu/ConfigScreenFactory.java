package com.github.puzzle.game.ui.modmenu;

import finalforeach.cosmicreach.gamestates.GameState;

public interface ConfigScreenFactory <T extends GameState> {
    T OnInitConfigScreen();
}
