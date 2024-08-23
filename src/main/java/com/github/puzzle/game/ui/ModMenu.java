package com.github.puzzle.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.puzzle.core.Puzzle;
import com.github.puzzle.game.ui.font.CosmicReachFont;
import com.github.puzzle.loader.mod.ModContainer;
import com.github.puzzle.loader.mod.ModLocator;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.MainMenu;
import finalforeach.cosmicreach.gamestates.PauseMenu;
import finalforeach.cosmicreach.lang.Lang;
import finalforeach.cosmicreach.ui.UIElement;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

import static com.badlogic.gdx.graphics.TextureData.TextureDataType.Pixmap;
import static com.badlogic.gdx.scenes.scene2d.ui.Table.Debug.actor;
import static com.badlogic.gdx.scenes.scene2d.ui.Table.Debug.table;

public class ModMenu extends GameState {

    public static NinePatch backgoundimg = new NinePatch(new Texture(GameAssetLoader.loadAsset("assets/puzzle-loader/icons/PuzzleLoaderIconx32.png")));

    Stage gdxStage;
    Viewport gdxStageViewport;
    OrthographicCamera gdxStageCamera;

    ScrollPane scrollPane;
    Button.ButtonStyle buttonStyle;
    Table modPage;
    Drawable backgroundTopBottom;
    Drawable modListBar;

    public ModMenu(GameState currentGameState) {
        this.previousState = currentGameState;
        buttonStyle = new Button.ButtonStyle();
        Pixmap pm = new Pixmap(1,1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pm.setColor(new Color(.10f,  .10f,  .10f, 1));
        pm.fill();
        backgroundTopBottom = new TextureRegionDrawable(new Texture(pm));
        pm.setColor(new Color(.10f,  .10f,  .10f, 1));
        pm.fill();
        modListBar = new TextureRegionDrawable(new Texture(pm));
        pm.dispose();
    }

    @Override
    public void create() {
        super.create();
        Gdx.gl.glDisable(2884);
        gdxStageCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gdxStageViewport = new ExtendViewport(800, 600, gdxStageCamera);
        gdxStage = new Stage(gdxStageViewport, batch);
        gdxStageCamera.position.set(0, 0, 0);
        Gdx.input.setInputProcessor(gdxStage);

        Table bottomBarLeft = new Table();
        Table bottomBarRight = new Table();
        Table topBarLeft = new Table();
        Table topBarRight = new Table();
        bottomBarLeft.background(backgroundTopBottom);
        bottomBarRight.background(backgroundTopBottom);
        topBarLeft.background(backgroundTopBottom);
        topBarRight.background(backgroundTopBottom);

        Table modPane = new Table();
        for(ModContainer m : ModLocator.locatedMods.values()) {
            Button button = new Button(buttonStyle);
            button.addListener(getClickListener(m, modPage));
            button.add(new Image(backgoundimg)).left().padLeft(5);
            button.add(new Label(m.NAME, new Label.LabelStyle(CosmicReachFont.FONT, Color.WHITE))).expand().center().padTop(15);
            modPane.add(button).minWidth(160).minHeight(50).pad(5);
            modPane.row();
        }

        scrollPane = new ScrollPane(modPane);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setSmoothScrolling(true);
        Container container = new Container(scrollPane);
        container.fill();
        container.padLeft(15).padRight(15);
        container.background(modListBar);

        Table root = new Table();
        Table topBar = root.add(topBarLeft).minHeight(40).maxHeight(40).maxWidth(160).fill().getActor();
        root.add(topBarRight).minHeight(40).maxHeight(40).top().left().expandX().fill();
        root.row();

        root.add(container).width(150).maxWidth(160).left().fill().expandY();
        modPage = root.add(new Table()).expandX().left().getActor();
        root.row();

        root.add(bottomBarLeft).minHeight(40).maxHeight(40).maxWidth(160).fill();
        root.add(bottomBarRight).minHeight(40).maxHeight(40).bottom().left().expandX().fill();
        root.setFillParent(true);

        Label title = new Label("MODS", new Label.LabelStyle(CosmicReachFont.FONT_BIG, Color.BLACK));
        title.layout();
        title.setSize(title.getGlyphLayout().width, title.getGlyphLayout().height);
        title.setPosition(200.0F, 100.0F, Align.center);
        gdxStage.addActor(root);
        gdxStage.setScrollFocus(scrollPane);

        Button backButton = new Button(buttonStyle);
        backButton.add(new Label("Done", new Label.LabelStyle(CosmicReachFont.FONT, Color.WHITE))).expand().center().padTop(15);
        backButton.addListener(getClickListener(this));
        bottomBarRight.add(backButton).right().expandX().padRight(20);
        //scrollPane.debugAll();
        //bottomBarRight.debugAll();
        //root.debugAll();
        //bottemBar.debugAll();
        //modPage.debugAll();

//        UIElement doneButton = new UIElement(0.0F, 200.0F, 250.0F, 50.0F) {
//            public void onClick() {
//                super.onClick();
//                ModMenu.this.returnToPrevious();
//            }
//        };
//        doneButton.setText(Lang.get("doneButton"));
//        doneButton.show();
//
//        uiObjects.add(doneButton);
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
        Gdx.gl.glClearColor( .20f,  .20f,  .20f, 1);
        Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        gdxStageViewport.apply(true);
        gdxStage.act(Gdx.graphics.getDeltaTime());
        gdxStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        //super.resize(width, height);
        gdxStage.getViewport().update(width, height, true);
        backgroundTopBottom.setMinWidth(width);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        gdxStage.act(deltaTime);
    }

    @Override
    public void switchAwayTo(GameState gameState) {
        super.switchAwayTo(gameState);
        Gdx.gl.glEnable(2884);
    }

    @Override
    public void dispose() {
        gdxStage.dispose();
    }

    private ClickListener getClickListener(ModContainer mod, Actor actor){
        return new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                //
            }
        };
    }

    private ClickListener getClickListener(ModMenu modMenu){
        return new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                GameState.switchToGameState(modMenu.previousState);
            }
        };
    }
}
