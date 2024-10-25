package com.github.puzzle.game.mixins.client.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.puzzle.core.loader.util.Reflection;
import com.github.puzzle.game.ClientGlobals;
import com.github.puzzle.game.ui.IGDXGameState;
import com.github.puzzle.game.ui.font.CosmicReachDefaultFont;
import finalforeach.cosmicreach.ClientZoneLoader;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.gamestates.*;
import finalforeach.cosmicreach.ui.UIElement;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.puzzle.game.assets.PuzzleGameAssetLoader.LOADER;

@Mixin(MainMenu.class)
public class MenuRedesignMixin extends GameState implements IGDXGameState {

    @Unique
    public Color bgColor = Color.BLACK.cpy();

    @Unique
    private Stage gdxStage;
    @Unique
    private Viewport gdxViewport;
    @Unique
    private OrthographicCamera gdxCamera;

    @Unique
    ImageTextButton.ImageTextButtonStyle regularStyle;
    @Unique
    ImageTextButton.ImageTextButtonStyle hoveredStyle;

    @Inject(method = "create", at = @At("HEAD"))
    public void create(CallbackInfo ci) {
        System.out.println("Created Menu");
        if (ClientGlobals.shouldUseRedesign.getValue()) {
            System.gc();
            GameSingletons.isHost = true;
            if (ClientZoneLoader.currentInstance != null) {
                ClientZoneLoader.currentInstance.setReadyToPlay(false);
            }

            System.out.println("Created Gdx Menu");
            gdxCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            gdxViewport = new ExtendViewport(800, 600, gdxCamera);
            gdxStage = new Stage(gdxViewport, batch);
            Gdx.input.setInputProcessor(gdxStage);

            gdxCamera.position.set(0, 0, 0);
            puzzleLoader$createUIElements();
        }
    }

    @Unique
    private ImageTextButton.ImageTextButtonStyle puzzleLoader$createHoveredStyle() {
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle();
        style.fontColor = Color.WHITE.cpy();
        style.font = CosmicReachDefaultFont.FONT_2X;
        style.unpressedOffsetY = -13;
        style.pressedOffsetY = -13;
        style.checkedOffsetY = -13;
        style.up = new TextureRegionDrawable(GameAssetLoader.getTexture("puzzle-loader:textures/ui/button_hover.png"));
//        style.down = new TextureRegionDrawable(GameAssetLoader.getTexture("puzzle-loader:textures/blocks/example_block.png"));
        return style;
    }

    @Unique
    private ImageTextButton.ImageTextButtonStyle puzzleLoader$createRegularStyle() {
        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle();
        style.fontColor = Color.WHITE.cpy();
        style.font = CosmicReachDefaultFont.FONT_2X;
        style.unpressedOffsetY = -13;
        style.pressedOffsetY = -13;
        style.checkedOffsetY = -13;
        style.up = new TextureRegionDrawable(GameAssetLoader.getTexture("puzzle-loader:textures/ui/button.png"));
//        style.down = new TextureRegionDrawable(GameAssetLoader.getTexture("puzzle-loader:textures/blocks/example_block.png"));
        return style;
    }

    @Unique
    private void puzzleLoader$addHoverEffect(ImageTextButton btn) {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(btn.getStyle().font, btn.getText());
        btn.setWidth(layout.width + 50);
        var act = new Action() {
            ImageTextButton.ImageTextButtonStyle pHoveredStyle;
            ImageTextButton.ImageTextButtonStyle pRegularStyle;

            public void init() {
                pRegularStyle = puzzleLoader$createRegularStyle();
                pHoveredStyle = puzzleLoader$createHoveredStyle();

                Texture texture = ((TextureRegion) Reflection.getFieldContents(pHoveredStyle.up, "region")).getTexture();
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
                pHoveredStyle.up = new TextureRegionDrawable(new Texture(newPix));

                texture = ((TextureRegion) Reflection.getFieldContents(pRegularStyle.up, "region")).getTexture();
                texture.getTextureData().prepare();
                pixmap = texture.getTextureData().consumePixmap();
                newPix = new Pixmap((int) btn.getWidth(), pixmap.getHeight(), pixmap.getFormat());
                for (int y = 0; y < newPix.getHeight(); y++) {
                    for (int x = 0; x < btn.getWidth(); x++) {
                        newPix.drawPixel(x, y, pixmap.getPixel(x, y));
                    }
                }
                texture.dispose();
                pRegularStyle.up = new TextureRegionDrawable(new Texture(newPix));
                System.gc();
            }
            boolean isHovered = false;

            @Override
            public boolean act(float v) {
                if (btn.isOver()) {
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
    private void puzzleLoader$createUIElements() {
        Image logo = new Image(LOADER.loadSync("base:textures/text-logo-hd.png", Texture.class));
        logo.setSize(768, 256);
        logo.setPosition(-680, 30);
        gdxStage.addActor(logo);

        regularStyle = puzzleLoader$createRegularStyle();
        hoveredStyle = puzzleLoader$createHoveredStyle();

        int btnY = 50;
        int btnStep = 56;
        int btnX = -460;

        ImageTextButton singlePlayerBtn = new ImageTextButton("SinglePlayer", regularStyle);
        singlePlayerBtn.setPosition(btnX, btnY -= btnStep);
        singlePlayerBtn.setSize(275, 50);
        singlePlayerBtn.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (GameState.currentGameState != MenuRedesignMixin.this) return;

                UIElement.onClickSound.play();
                GameState.switchToGameState(new WorldSelectionMenu());
            }
        });
        puzzleLoader$addHoverEffect(singlePlayerBtn);
        gdxStage.addActor(singlePlayerBtn);


        ImageTextButton multiplayerBtn = new ImageTextButton("Multiplayer", regularStyle);
        multiplayerBtn.setPosition(btnX, btnY -= btnStep);
        multiplayerBtn.setSize(275, 50);
        multiplayerBtn.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (GameState.currentGameState != MenuRedesignMixin.this) return;

                UIElement.onClickSound.play();
                GameState.switchToGameState(new NetworkMenu(MenuRedesignMixin.this));
            }
        });
        puzzleLoader$addHoverEffect(multiplayerBtn);
        gdxStage.addActor(multiplayerBtn);

        ImageTextButton optionsBtn = new ImageTextButton("Options", regularStyle);
        optionsBtn.setPosition(btnX, btnY -= btnStep);
        optionsBtn.setSize(275, 50);
        optionsBtn.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (GameState.currentGameState != MenuRedesignMixin.this) return;

                UIElement.onClickSound.play();
                GameState.switchToGameState(new OptionsMenu(MenuRedesignMixin.this));
            }
        });
        puzzleLoader$addHoverEffect(optionsBtn);
        gdxStage.addActor(optionsBtn);

        ImageTextButton modsBtn = new ImageTextButton("Mods", regularStyle);
        modsBtn.setPosition(btnX, btnY -= btnStep);
        modsBtn.setSize(275, 50);
        modsBtn.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (GameState.currentGameState != MenuRedesignMixin.this) return;

                UIElement.onClickSound.play();
            }
        });
        puzzleLoader$addHoverEffect(modsBtn);
        gdxStage.addActor(modsBtn);

        ImageTextButton quitBtn = new ImageTextButton("Quit", regularStyle);
        quitBtn.setPosition(btnX, btnY - btnStep);
        quitBtn.setSize(275, 50);
        quitBtn.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (GameState.currentGameState != MenuRedesignMixin.this) return;

                UIElement.onClickSound.play();
                System.exit(0);
            }
        });
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
            gdxStage.getViewport().apply(false);
            gdxStage.act();
            gdxStage.draw();
            Gdx.gl.glEnable(GL20.GL_CULL_FACE);
            ci.cancel();
        }
    }

    @Override
    public void switchAwayTo(GameState gameState) {
        if (gameState instanceof IGDXGameState igdxGameState) {
            Gdx.input.setInputProcessor(null);
        }
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
