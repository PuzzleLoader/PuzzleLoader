package com.github.puzzle.game.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.puzzle.game.ui.gamestate.IGDXGameState;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.items.ISlotContainerParent;
import finalforeach.cosmicreach.ui.UI;
import jdk.jfr.Experimental;

@Experimental
public class BaseGDXScreen extends BasePuzzleScreen implements IGDXGameState {

    public InputProcessor oldProcessor;

    public Stage gdxStage;
    public Color bgColor;

    public BaseGDXScreen(ISlotContainerParent parent) {
        super(parent);

        gdxStage = new Stage(InGame.IN_GAME.ui.uiViewport, UI.batch);
    }

    @Override
    public void drawItems() {
        onRender();
        super.drawItems();
    }

    public void onRender() {
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);
        gdxStage.getViewport().apply(false);
        gdxStage.act();
        gdxStage.draw();
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
    }

    @Override
    public Stage getStage() {
        return gdxStage;
    }

    @Override
    public Camera getCamera() {
        return InGame.IN_GAME.ui.uiViewport.getCamera();
    }

    @Override
    public Viewport getViewport() {
        return gdxStage.getViewport();
    }

    @Override
    public Color backgroundColor() {
        return bgColor;
    }

    @Override
    public void onOpen() {
        oldProcessor = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(gdxStage);
    }

    @Override
    public void onClose() {
        Gdx.input.setInputProcessor(oldProcessor);
    }
}
