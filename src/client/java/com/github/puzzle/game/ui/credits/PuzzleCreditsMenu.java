package com.github.puzzle.game.ui.credits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.puzzle.game.resources.PuzzleGameAssetLoader;
import com.github.puzzle.game.ui.credits.categories.ICreditElement;
import com.github.puzzle.game.ui.credits.categories.ImageCredit;
import finalforeach.cosmicreach.gamestates.CreditsMenu;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.LanguagesMenu;
import finalforeach.cosmicreach.gamestates.MainMenu;
import finalforeach.cosmicreach.lang.Lang;
import finalforeach.cosmicreach.settings.Controls;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.UIElement;
import finalforeach.cosmicreach.ui.VerticalAnchor;
import finalforeach.cosmicreach.ui.widgets.CRButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("removal")
public class PuzzleCreditsMenu extends GameState {

    public static final List<ICreditElement> categories = new ArrayList<>();

    public PuzzleCreditsMenu() {}

    public static void addCategory(ICreditElement category) {
        categories.add(category);
    }

    public static void addFile(CreditFile file) {
        categories.addAll(file.categories);
    }

    @Override
    public void create() {
        super.create();

        ImageCredit image = new ImageCredit(PuzzleGameAssetLoader.LOADER.loadSync("base:textures/text-logo-hd.png", Texture.class));
        image.setScale(0.5f);
        PuzzleCreditsMenu.addCategory(image);
        new CreditsMenu().create();
        PuzzleCreditsMenu.addFile(CreditFile.fromVanilla(Gdx.files.classpath("post_build/Cosmic-Reach-Localization/CREDITS.txt").readString()));

//        CRButton returnBtn = new CRButton(Lang.get("Return To Main Menu"));
//        returnBtn.setPosition(0, 0, Align.bottomRight);
        UIElement returnButton = new UIElement(-16.0F, -16.0F, 250.0F, 50.0F) {
            public void onClick() {
                super.onClick();
                GameState.switchToGameState(new MainMenu());
            }
        };
        returnButton.hAnchor = HorizontalAnchor.RIGHT_ALIGNED;
        returnButton.vAnchor = VerticalAnchor.BOTTOM_ALIGNED;
        returnButton.setText(Lang.get("Return_to_Main_Menu"));
        returnButton.show();

//        stage.addActor(returnBtn);
        this.uiObjects.add(returnButton);
    }

    float animationTime;

    @Override
    public void render() {
        if (Lang.currentLang == null) {
            GameState.switchToGameState(new LanguagesMenu(this));
        } else {
            super.render();

            float scrollSpeed = 30.0F;
            float yOff = -this.animationTime;
            if (Controls.jumpPressed() || Controls.sprintPressed()) {
                scrollSpeed *= 10.0F;
            }

            ScreenUtils.clear(0.0F, 0.0F, 0.0F, 1.0F, true);
            Gdx.gl.glEnable(2929);
            Gdx.gl.glDepthFunc(513);
            Gdx.gl.glEnable(2884);
            Gdx.gl.glCullFace(1029);
            Gdx.gl.glEnable(3042);
            Gdx.gl.glBlendFunc(770, 771);
            batch.setProjectionMatrix(this.uiCamera.combined);
            batch.begin();
            AtomicReference<Float> y = new AtomicReference<>(-this.uiViewport.getWorldHeight() / 2.0F + 16.0F + yOff);

            for (ICreditElement creditElement : categories) {
                y.updateAndGet(v -> v + creditElement.getTopPadding());
                creditElement.render(batch, uiViewport, (p) -> y.updateAndGet(v -> v + p));
                y.updateAndGet(v -> v + creditElement.getBottomPadding() + creditElement.getHeight());
            }

            this.animationTime += Gdx.graphics.getDeltaTime() * scrollSpeed;
            if (y.get() < -this.uiViewport.getWorldHeight() / 2.0F) {
                this.animationTime = -this.uiViewport.getWorldHeight();
            }

            batch.end();
            this.drawUIElements();
        }
    }
}
