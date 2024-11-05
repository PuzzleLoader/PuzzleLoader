package com.github.puzzle.game.ui.gamestate;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public interface IGDXGameState {

    Stage getStage();
    Camera getCamera();
    Viewport getViewport();

    Color backgroundColor();

}
