package com.github.puzzle.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.puzzle.game.ui.font.CosmicReachFont;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.MainMenu;
import finalforeach.cosmicreach.gamestates.PauseMenu;
import finalforeach.cosmicreach.lang.Lang;
import finalforeach.cosmicreach.ui.UIElement;
import org.lwjgl.opengl.GL11;

public class ModMenu extends GameState {

    public static NinePatch backgoundimg = new NinePatch(new Texture(GameAssetLoader.loadAsset("assets/puzzle-loader/textures/ui/background.png")));

    Stage gdxStage;
    Viewport gdxStageViewport;
    OrthographicCamera gdxStageCamera;

    ScrollPane scrollPane;
    ButtonGroup<TextButton> modButtons;

    public ModMenu(GameState currentGameState) {
        this.previousState = currentGameState;
    }

    @Override
    public void create() {
        super.create();
        Gdx.gl.glDisable(2884);
        gdxStageCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gdxStageViewport = new ExtendViewport(800.0F, 600.0F, gdxStageCamera);
        gdxStage = new Stage(gdxStageViewport, batch);
        gdxStageCamera.position.set(0, 0, 0);
        Image background = new Image(backgoundimg);
        background.setHeight(Gdx.graphics.getHeight());
        background.setWidth(Gdx.graphics.getWidth());
        background.setFillParent(true);


        Label title = new Label("MODS", new Label.LabelStyle(CosmicReachFont.FONT_BIG, Color.BLACK));
        title.layout();
        title.setSize(title.getGlyphLayout().width, title.getGlyphLayout().height);
        title.setPosition(200.0F, 100.0F, Align.center);
        gdxStage.addActor(background);
        gdxStage.addActor(title);


        UIElement doneButton = new UIElement(0.0F, 200.0F, 250.0F, 50.0F) {
            public void onClick() {
                super.onClick();
                ModMenu.this.returnToPrevious();
            }
        };
        doneButton.setText(Lang.get("doneButton"));
        doneButton.show();

        /* TODO: Add list
        for(ModContainer m : ModLocator.locatedMods.values()){
           TextButton button = new TextButton(m.NAME, new Skin());
           button.setLabel(new Label(m.NAME,textStyle));
        }
         */
        uiObjects.add(doneButton);
    }

    private void returnToPrevious() {
        if (this.previousState instanceof MainMenu) {
            switchToGameState(new MainMenu());
        } else if (this.previousState instanceof PauseMenu) {
            switchToGameState(new PauseMenu(((PauseMenu)this.previousState).cursorCaught));
        } else {
            switchToGameState(this.previousState);
        }

    }
    public void render() {
        super.render();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            this.returnToPrevious();
        }
        Gdx.gl.glClearColor( .25f,  .25f,  .25f, 1);
        Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        drawUIElements();
        gdxStageViewport.apply(false);
        gdxStage.act();
        gdxStage.draw();
    }
    @Override
    public void resize(int width, int height) {
        //super.resize(width, height);
        gdxStage.getViewport().update(width, height, true);
        gdxStageViewport.update(width, height, false);
    }

    @Override
    public void update(float deltaTime) {
        //super.update(deltaTime);
        gdxStage.act(deltaTime);
    }

    @Override
    public void switchAwayTo(GameState gameState) {
        super.switchAwayTo(gameState);
        Gdx.gl.glEnable(2884);
    }
}
