package com.github.puzzle.game.ui.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import finalforeach.cosmicreach.gamestates.GameState;
import org.lwjgl.opengl.GL11;

// TODO: Work on this
public abstract class GDXGameState extends GameState implements IGDXGameState {

    public Stage gdxStage;
    public Viewport gdxViewport;
    public OrthographicCamera gdxCamera;

    public Color bgColor;

    public GDXGameState() {
        // Make Sure Everything is cleared and destroyed
        uiObjects.clear();
        newUiCamera = null;
        uiCamera = null;
        uiViewport = null;
        newUiViewport = null;

        System.gc();
    }

    @Override
    public void create() {
        // Initialize GDXGameState requirements
        gdxCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gdxViewport = new ExtendViewport(800, 600, gdxCamera);
        gdxStage = new Stage(gdxViewport, batch);

        gdxCamera.position.set(0, 0, 0);
    }

    @Override
    public void render() {
        super.render();
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);
        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
        Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        gdxStage.getViewport().apply(false);
        gdxStage.act();
        gdxStage.draw();
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
    }

    @Override
    @Deprecated
    public void drawUIElements() {}

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        gdxViewport.update(width, height, false);
    }

    @Override
    public void switchAwayTo(GameState gameState) {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void onSwitchTo() {
        Gdx.input.setInputProcessor(gdxStage);
    }

    @Override
    public Stage getStage() {
        return gdxStage;
    }

    @Override
    public Viewport getViewport() {
        return gdxViewport;
    }

    @Override
    public Camera getCamera() {
        return gdxCamera;
    }
}
