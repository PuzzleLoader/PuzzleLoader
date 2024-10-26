package com.github.puzzle.game.mixins.client.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.puzzle.core.loader.util.Reflection;
import com.github.puzzle.game.ClientGlobals;
import com.github.puzzle.game.assets.PuzzleGameAssetLoader;
import com.github.puzzle.game.mod.internal.EnvironmentCubemap;
import com.github.puzzle.game.ui.IGDXGameState;
import com.github.puzzle.game.ui.elements.CommonButton;
import com.github.puzzle.game.ui.elements.GDXButton;
import com.github.puzzle.game.ui.font.CosmicReachDefaultFont;
import com.github.puzzle.game.ui.menus.credits.PuzzleCreditsMenu;
import finalforeach.cosmicreach.ClientZoneLoader;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.gamestates.*;
import finalforeach.cosmicreach.ui.UIElement;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.function.Function;

import static com.github.puzzle.game.assets.PuzzleGameAssetLoader.LOADER;

@Mixin(MainMenu.class)
public class MenuRedesignMixin extends GameState implements IGDXGameState {

    @Shadow private PerspectiveCamera rawWorldCamera;
    private static HashMap<String, GDXButton.GDXButtonStyle> STYLES = new HashMap<>();
    private static TextureRegionDrawable BUTTON_HOVER;
    private static TextureRegionDrawable BUTTON_REGULAR;

    @Unique
    public Color bgColor = Color.BLACK.cpy();

    @Unique
    private Stage gdxStage;
    @Unique
    private Viewport gdxViewport;
    @Unique
    private OrthographicCamera gdxCamera;

    @Unique
    GDXButton.GDXButtonStyle regularStyle;
    @Unique
    GDXButton.GDXButtonStyle hoveredStyle;

    @Inject(method = "create", at = @At("HEAD"))
    public void create(CallbackInfo ci) {
        if (ClientGlobals.shouldUseRedesign.getValue()) {
            System.gc();
            GameSingletons.isHost = true;
            if (ClientZoneLoader.currentInstance != null) {
                ClientZoneLoader.currentInstance.setReadyToPlay(false);
            }

            gdxCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            gdxViewport = new ExtendViewport(800, 600, gdxCamera);
            gdxStage = new Stage(gdxViewport, batch);
            Gdx.input.setInputProcessor(gdxStage);

            gdxCamera.position.set(0, 0, 0);
            if (BUTTON_HOVER == null) BUTTON_HOVER = new TextureRegionDrawable(GameAssetLoader.getTexture("puzzle-loader:textures/ui/button_hover.png"));
            if (BUTTON_REGULAR == null) BUTTON_REGULAR = new TextureRegionDrawable(GameAssetLoader.getTexture("puzzle-loader:textures/ui/button.png"));
            puzzleLoader$createUIElements();
        }
    }

    // Style Caching
    @Unique
    private GDXButton.GDXButtonStyle puzzleLoader$createHoveredStyle() {
        return puzzleLoader$createHoveredStyle("GDX_HOVERED", s -> s);
    }

    @Unique
    private GDXButton.GDXButtonStyle puzzleLoader$createHoveredStyle(String key, Function<GDXButton.GDXButtonStyle, GDXButton.GDXButtonStyle> modifier) {
        if (STYLES.containsKey(key)) return STYLES.get(key);

        GDXButton.GDXButtonStyle style = new GDXButton.GDXButtonStyle();
        style.fontColor = Color.WHITE.cpy();
        style.font = CosmicReachDefaultFont.FONT_2X;
        style.unpressedOffsetY = -13;
        style.pressedOffsetY = -13;
        style.checkedOffsetY = -13;
        style.up = BUTTON_HOVER;
        STYLES.put(key, modifier.apply(style));
        return style;
    }

    @Unique
    private GDXButton.GDXButtonStyle puzzleLoader$createRegularStyle() {
        return puzzleLoader$createRegularStyle("GDX_REGULAR", s -> s);
    }

    @Unique
    private GDXButton.GDXButtonStyle puzzleLoader$createRegularStyle(String key, Function<GDXButton.GDXButtonStyle, GDXButton.GDXButtonStyle> modifier) {
        if (STYLES.containsKey(key)) return STYLES.get(key);

        GDXButton.GDXButtonStyle style = new GDXButton.GDXButtonStyle();
        style.fontColor = Color.WHITE.cpy();
        style.font = CosmicReachDefaultFont.FONT_2X;
        style.unpressedOffsetY = -13;
        style.pressedOffsetY = -13;
        style.checkedOffsetY = -13;
        style.up = BUTTON_REGULAR;
        STYLES.put(key, modifier.apply(style));
        return style;
    }

    @Unique
    private void puzzleLoader$addHoverEffect(GDXButton btn) {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(btn.getStyle().font, btn.getText());
        btn.setWidth(layout.width + 50);
        var act = new Action() {
            GDXButton.GDXButtonStyle pHoveredStyle;
            GDXButton.GDXButtonStyle pRegularStyle;

            public void init() {
                pRegularStyle = puzzleLoader$createRegularStyle(btn.getText() + "BTN_REGULAR", (s) -> {
                    Texture texture = ((TextureRegion) Reflection.getFieldContents(s.up, "region")).getTexture();
                    texture.getTextureData().prepare();
                    Pixmap pixmap = texture.getTextureData().consumePixmap();
                    Pixmap newPix = new Pixmap((int) btn.getWidth(), pixmap.getHeight(), pixmap.getFormat());
                    newPix.drawPixmap(pixmap, 0, 0);
                    for (int y = 0; y < newPix.getHeight(); y++) {
                        for (int x = 0; x < btn.getWidth(); x++) {
                            newPix.drawPixel(x, y, pixmap.getPixel(x, y));
                        }
                    }
                    texture.dispose();
                    s.up = new TextureRegionDrawable(new Texture(newPix));
                    return s;
                });
                pHoveredStyle = puzzleLoader$createHoveredStyle(btn.getText() + "BTN_HOVER", (s) -> {
                    Texture texture = ((TextureRegion) Reflection.getFieldContents(s.up, "region")).getTexture();
                    texture.getTextureData().prepare();
                    Pixmap pixmap = texture.getTextureData().consumePixmap();
                    Pixmap newPix = new Pixmap((int) btn.getWidth(), pixmap.getHeight(), pixmap.getFormat());
                    newPix.drawPixmap(pixmap, 0, 0);
                    for (int y = 0; y < newPix.getHeight(); y++) {
                        for (int x = 0; x < btn.getWidth(); x++) {
                            newPix.drawPixel(x, y, pixmap.getPixel(x, y));
                        }
                    }
                    texture.dispose();
                    s.up = new TextureRegionDrawable(new Texture(newPix));
                    return s;
                });
                System.gc();
            }
            boolean isHovered = false;

            @Override
            public boolean act(float v) {
                if (btn.isHoveringOver()) {
                    if (!isHovered) {
                        UIElement.onHoverSound.play();
                    }
                    btn.setStyle(pHoveredStyle);
                    isHovered = true;
                } else {
                    btn.setStyle(pRegularStyle);
                    isHovered = false;
                }
                return false;
            }
        };
        act.init();
        btn.addAction(act);
    }

    @Unique
    EnvironmentCubemap envCubemap;

    @Unique
    private void puzzleLoader$createUIElements() {
         envCubemap = new EnvironmentCubemap(
                 PuzzleGameAssetLoader.locateAsset("puzzle-loader:textures/panorama/px.png"),
                 PuzzleGameAssetLoader.locateAsset("puzzle-loader:textures/panorama/nx.png"),
                 PuzzleGameAssetLoader.locateAsset("puzzle-loader:textures/panorama/py.png"),
                 PuzzleGameAssetLoader.locateAsset("puzzle-loader:textures/panorama/ny.png"),
                 PuzzleGameAssetLoader.locateAsset("puzzle-loader:textures/panorama/pz.png"),
                 PuzzleGameAssetLoader.locateAsset("puzzle-loader:textures/panorama/nz.png")
         );

        Image logo = new Image(LOADER.loadSync("base:textures/text-logo-hd.png", Texture.class));
        logo.setSize(768, 256);
        logo.setPosition(-680, 30);
        gdxStage.addActor(logo);

        regularStyle = puzzleLoader$createRegularStyle();
        hoveredStyle = puzzleLoader$createHoveredStyle();

        int btnY = 50;
        int btnStep = 56;
        int btnX = -460;

        GDXButton singlePlayerBtn = new GDXButton("SinglePlayer", regularStyle) {
            @Override
            public void onClick(InputEvent event, float mouseX, float mouseY) {
                super.onClick(event, mouseX, mouseY);
                UIElement.onClickSound.play();
                GameState.switchToGameState(new WorldSelectionMenu());
            }
        };
        singlePlayerBtn.setPosition(btnX, btnY -= btnStep);
        singlePlayerBtn.setSize(275, 50);
        puzzleLoader$addHoverEffect(singlePlayerBtn);
        gdxStage.addActor(singlePlayerBtn);

//        CommonButton singlePlayerBtn = new CommonButton(btnX, btnY -= btnStep, 250, 50, "SinglePlayer") {
//            @Override
//            public void onClick(InputEvent event, float mouseX, float mouseY) {
//                super.onClick(event, mouseX, mouseY);
//                GameState.switchToGameState(new WorldSelectionMenu());
//            }
//        };
//        gdxStage.addActor(singlePlayerBtn);

        GDXButton multiplayerBtn = new GDXButton("Multiplayer", regularStyle) {
            @Override
            public void onClick(InputEvent event, float mouseX, float mouseY) {
                super.onClick(event, mouseX, mouseY);
                UIElement.onClickSound.play();
                GameState.switchToGameState(new NetworkMenu(MenuRedesignMixin.this));
            }
        };
        multiplayerBtn.setPosition(btnX, btnY -= btnStep);
        multiplayerBtn.setSize(275, 50);
        puzzleLoader$addHoverEffect(multiplayerBtn);
        gdxStage.addActor(multiplayerBtn);

        GDXButton optionsBtn = new GDXButton("Options", regularStyle) {
            @Override
            public void onClick(InputEvent event, float mouseX, float mouseY) {
                super.onClick(event, mouseX, mouseY);
                UIElement.onClickSound.play();
                GameState.switchToGameState(new OptionsMenu(MenuRedesignMixin.this));
            }
        };
        optionsBtn.setPosition(btnX, btnY -= btnStep);
        optionsBtn.setSize(275, 50);
        puzzleLoader$addHoverEffect(optionsBtn);
        gdxStage.addActor(optionsBtn);

        GDXButton modsBtn = new GDXButton("Credits", regularStyle) {
            @Override
            public void onClick(InputEvent event, float mouseX, float mouseY) {
                super.onClick(event, mouseX, mouseY);
                UIElement.onClickSound.play();
                GameState.switchToGameState(new PuzzleCreditsMenu());
            }
        };
        modsBtn.setPosition(btnX, btnY -= btnStep);
        modsBtn.setSize(275, 50);
        puzzleLoader$addHoverEffect(modsBtn);
        gdxStage.addActor(modsBtn);

        GDXButton quitBtn = new GDXButton("Quit", regularStyle) {
            @Override
            public void onClick(InputEvent event, float mouseX, float mouseY) {
                super.onClick(event, mouseX, mouseY);
                UIElement.onClickSound.play();
                System.exit(0);
            }
        };
        quitBtn.setPosition(btnX, btnY - btnStep);
        quitBtn.setSize(275, 50);
        puzzleLoader$addHoverEffect(quitBtn);
        gdxStage.addActor(quitBtn);
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void render(CallbackInfo ci) {

        if (ClientGlobals.shouldUseRedesign.getValue()) {
            super.render();
            Gdx.gl.glDisable(GL20.GL_CULL_FACE);
            Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
            Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            envCubemap.render(rawWorldCamera);
            gdxStage.getViewport().apply(false);
            gdxStage.act();
            gdxStage.draw();
            Gdx.gl.glEnable(GL20.GL_CULL_FACE);
            ci.cancel();
        }
    }

    @Override
    public void switchAwayTo(GameState gameState) {
        // Disable input after leaving gamestate
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        gdxViewport.update(width, height, false);
    }

    @Override
    public Stage getStage() {
        return gdxStage;
    }

    @Override
    public Camera getCamera() {
        return gdxCamera;
    }

    @Override
    public Viewport getViewport() {
        return gdxViewport;
    }
}
