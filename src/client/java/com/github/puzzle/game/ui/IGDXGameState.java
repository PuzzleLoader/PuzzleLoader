package com.github.puzzle.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import finalforeach.cosmicreach.gamestates.GameState;

public interface IGDXGameState {

    Stage getStage();
    Camera getCamera();
    Viewport getViewport();

    default void switchAwayTo(GameState gameState) {
        if (gameState instanceof IGDXGameState igdxGameState) {
            Gdx.input.setInputProcessor(null);
        }
    }

}
