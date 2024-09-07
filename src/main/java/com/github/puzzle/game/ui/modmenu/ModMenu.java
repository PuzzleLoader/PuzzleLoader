package com.github.puzzle.game.ui.modmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.puzzle.core.PuzzleRegistries;
import com.github.puzzle.game.events.OnRegisterModMenuTable;
import com.github.puzzle.game.ui.font.CosmicReachFont;
import com.github.puzzle.loader.mod.ModContainer;
import com.github.puzzle.loader.mod.ModLocator;
import com.github.puzzle.loader.mod.info.ModInfo;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.MainMenu;
import finalforeach.cosmicreach.gamestates.PauseMenu;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.util.HashMap;

import static com.github.puzzle.core.resources.PuzzleGameAssetLoader.LOADER;
import static com.github.puzzle.game.ui.font.CosmicReachFont.createCosmicReachFont;

public class ModMenu extends GameState {

    private static final ArrayMap<String, Table> customTables = new ArrayMap<>();
    private static final HashMap<String, ConfigScreenFactory<?>> customConfigScreens = new HashMap<>();

    Stage gdxStage;
    Viewport gdxStageViewport;
    OrthographicCamera gdxStageCamera;

    ScrollPane scrollPane;
    Button.ButtonStyle buttonStyle;
    BitmapFont modlablefont = createCosmicReachFont();
    BitmapFont versonfont = createCosmicReachFont();
    Texture cog = LOADER.loadSync("assets/puzzle-loader/textures/ui/cog.png", Texture.class);

    public ModMenu(GameState currentGameState) {
        this.previousState = currentGameState;
        buttonStyle = new Button.ButtonStyle();
        buttonStyle.up = genColor(.231f,.231f, .231f);
        buttonStyle.down = genColor(.331f,.331f, .331f);
        versonfont.getData().setScale(0.6F);
        modlablefont.getData().setScale(0.9F);
    }

    public static void registerModConfigScreen(String modID, ConfigScreenFactory<?> configScreen) {
        customConfigScreens.put(modID, configScreen);
    }

    @Override
    public void create() {
        super.create();
        gdxStageCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gdxStageViewport = new ExtendViewport(800, 600, gdxStageCamera);
        gdxStage = new Stage(gdxStageViewport, batch);
        gdxStageCamera.position.set(0, 0, 0);
        Gdx.input.setInputProcessor(gdxStage);

        Table modPanel = new Table();
        scrollPane = new ScrollPane(modPanel);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setSmoothScrolling(true);
        Table baseTable = new Table();
        Table leftTable = new Table();
        leftTable.background(genColor(.141f, .141f, .141f));
        Table righTable = new Table();
        righTable.background(genColor(.31f, .31f, .31f));

        for(ModContainer m : ModLocator.locatedMods.values()) {
            String path = m.INFO.Metadata.get("icon") != null ? m.INFO.Metadata.get("icon").asString() : "assets/puzzle-loader/icons/example.png";
            Image icon = new Image(LOADER.loadSync(path, Texture.class));
            Table rightSide = new Table();
            Label modname = new Label(m.NAME, new Label.LabelStyle(modlablefont, Color.WHITE));
            Label modversion = new Label("v"+m.VERSION.toString() , new Label.LabelStyle(versonfont, Color.WHITE));
            modname.setAlignment(Align.bottom);
            modversion.setAlignment(Align.center);
            Button button = new Button(buttonStyle);
            button.addListener(getClickListener(m, righTable));
            button.add(icon).width(32).height(32).left().padLeft(5).expandY().fill();
            button.add(rightSide).padLeft(4).expand().fill();
            rightSide.add(modname).height(25).left().bottom().expandX().row();
            rightSide.add(modversion).height(25).left().padTop(3).bottom().expandX();
            modPanel.add(button).minWidth(150).minHeight(50).pad(5).top().fill();
            modPanel.row();
        }
        modPanel.add().grow();

        Label title = new Label("Mods", new Label.LabelStyle(CosmicReachFont.FONT_BIG, Color.WHITE));
        title.setSize(title.getGlyphLayout().width, title.getGlyphLayout().height);
        title.setAlignment(Align.center);

        Button backButton = new Button(buttonStyle);
        backButton.add(new Label("Done", new Label.LabelStyle(CosmicReachFont.FONT, Color.WHITE))).expand().center().padTop(15);
        backButton.background(genColor(.231f,.231f, .231f));
        backButton.addListener(getClickListener(this));

        leftTable.add(title).width(leftTable.getWidth()).padTop(50).padBottom(10).center().fill().row();
        leftTable.add(scrollPane).center().expand().fill().row();
        leftTable.add(backButton).height(30).width(150).bottom().padBottom(5);

        baseTable.add(leftTable).left().width(170).expandY().fill();
        baseTable.add(righTable).pad(10).expand().fill();
        baseTable.setFillParent(true);

        gdxStage.addActor(baseTable);
        gdxStage.setScrollFocus(scrollPane);
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
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            this.returnToPrevious();
        }
        Gdx.gl.glClearColor( 0.212f,  0.212f,  0.212f, 1);
        Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        gdxStageViewport.apply(true);
        gdxStage.act(Gdx.graphics.getDeltaTime());
        gdxStage.draw();
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
    }

    @Override
    public void resize(int width, int height) {
        gdxStage.getViewport().update(width, height, true);
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

    private ClickListener getClickListener(ModContainer mod, Table table) {
        return new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                //loadConfigButton(mod);


                table.clear();
                ModInfo info = mod.INFO;
                ScrollPane bottomBar = customTables.get(mod.ID) == null ? new ScrollPane(getDefaultTable()) : new ScrollPane(customTables.get(mod.ID));
                Table topBar = new Table();
                Table topRightBar = new Table();
                topRightBar.setBackground(genColor(.196f,.196f, .196f));
                String path = info.Metadata.get("icon") != null ? info.Metadata.get("icon").asString() : "assets/puzzle-loader/icons/example.png";
                Image icon = new Image(LOADER.loadSync(path, Texture.class));
                Label modname = new Label(mod.NAME, new Label.LabelStyle(CosmicReachFont.FONT_BIG, Color.WHITE));
                modname.setAlignment(Align.bottomLeft);
                Label descriptionLabel = new Label(info.Description, new Label.LabelStyle(modlablefont, Color.WHITE));
                descriptionLabel.setAlignment(Align.center);
                descriptionLabel.setWrap(true);

                Container<Image> container1 = new Container<Image>(icon).center().height(180).width(180);
                container1.setBackground(genColor(.196f,.196f, .196f));
                Container<Label> container3 = new Container<>(descriptionLabel).fillX();
                container3.setBackground(genColor(.196f,.196f, .196f));
                Container<ScrollPane> scrollContainer = new Container<>(bottomBar).fill();
                scrollContainer.setBackground(genColor(.196f,.196f, .196f));

                String str = "  Authors: ";
                String[] authors = info.Authors.toArray(new String[0]);
                for (int i = 0; i < authors.length; i++) {
                    str += authors[i];
                    if(i < authors.length - 1) str += ", ";
                }
                Label authorsLabel = new Label(str, new Label.LabelStyle(modlablefont, Color.WHITE));
                Container<Label> authorsLabelContainer = new Container<>(authorsLabel).fill();
                authorsLabelContainer.setBackground(genColor(.231f,.231f,.231f));

                Table modNameAndConfigButton = new Table();
                modNameAndConfigButton.add(modname).height(100).left().expandX().fill();
                modNameAndConfigButton.add(loadConfigButton(mod)).top().pad(5).maxWidth(30).maxHeight(30).minHeight(30).minWidth(30).row();
                modNameAndConfigButton.add().expand();
                //topRightBar.add(topRightBarButtons).width(50).height(50).padTop(10).padRight(10).expandX().fill().right().row();
                topRightBar.add(modNameAndConfigButton).pad(10).expandX().left().fill().row();
                topRightBar.add(authorsLabelContainer).pad(10).expand().fill();
                //topRightBar.debugAll();

                topBar.add(container1).height(200).width(200).left().pad(10);
                topBar.add(topRightBar).expand().fill().padRight(10).padTop(10).padBottom(10).row();

                table.add(topBar).top().expandX().fill().row();
                table.add(container3).height(80).expandX().fill().padRight(10).padLeft(10).row();
                table.add(scrollContainer).expand().fill().pad(10);
            }
        };
    }

    private ImageButton loadConfigButton(ModContainer mod) {
        ConfigScreenFactory<?> configScreenFactory = customConfigScreens.get(mod.ID);
        if(configScreenFactory == null) return null;
        GameState modMenu = this;

        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
        imageButtonStyle.up = genColor(.231f,.231f, .231f);
        imageButtonStyle.down = genColor(.331f,.331f, .331f);
        imageButtonStyle.imageUp = new SpriteDrawable(new Sprite(cog));
        ImageButton imageButton = new ImageButton(imageButtonStyle);
        imageButton.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    GameState gameState = configScreenFactory.OnInitConfigScreen();
                    gameState.previousState = modMenu;
                    switchToGameState(gameState);
                }
        });
        return imageButton;
    }

    private ClickListener getClickListener(ModMenu modMenu){
        return new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                GameState.switchToGameState(modMenu.previousState);
            }
        };
    }

    private TextureRegionDrawable genColor(float v1, float v2, float v3) {
        Pixmap pm = new Pixmap(1,1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pm.setColor(new Color(v1,  v2,  v3, 1));
        pm.fill();
        TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(new Texture(pm));
        pm.dispose();
        return textureRegionDrawable;
    }

    private Table getDefaultTable(){
        Table table = new Table();
        return table;
    }

    static {
        PuzzleRegistries.EVENT_BUS.post(new OnRegisterModMenuTable(customTables));
    }
}
