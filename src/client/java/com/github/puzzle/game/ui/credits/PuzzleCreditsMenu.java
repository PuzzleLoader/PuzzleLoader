package com.github.puzzle.game.ui.credits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.puzzle.game.engine.rendering.text.FormatText;
import com.github.puzzle.game.engine.rendering.text.FormattedTextRenderer;
import com.github.puzzle.game.resources.PuzzleGameAssetLoader;
import com.github.puzzle.game.ui.credits.categories.CreditCategory;
import com.github.puzzle.game.ui.credits.categories.ICreditElement;
import com.github.puzzle.game.ui.credits.categories.TextureCreditElement;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.LanguagesMenu;
import finalforeach.cosmicreach.gamestates.MainMenu;
import finalforeach.cosmicreach.lang.Lang;
import finalforeach.cosmicreach.settings.Controls;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.UIElement;
import finalforeach.cosmicreach.ui.VerticalAnchor;

import java.util.ArrayList;
import java.util.List;

import static finalforeach.cosmicreach.ui.FontRenderer.getTextDimensions;

public class PuzzleCreditsMenu extends GameState {

    public static final List<ICreditElement> categories = new ArrayList<>();
    private static final Vector2 v2 = new Vector2();

    public PuzzleCreditsMenu() {}

    public void addCategory(ICreditElement category) {
        categories.add(category);
    }

    public void addFile(CreditFile file) {
        categories.addAll(file.categories);
    }

    @Override
    public void create() {
        super.create();

        categories.add(new TextureCreditElement(MainMenu.textLogo));
//        categories.add(new TextureCreditElement(LOADER.loadSync("puzzle-loader:icons/PuzzleLoaderIconx160.png", Texture.class)));

        addFile(CreditFile.fromJson(PuzzleGameAssetLoader.locateAsset("puzzle-loader:credits.json").readString()));
        addFile(CreditFile.fromVanilla(Gdx.files.classpath("post_build/Cosmic-Reach-Localization/CREDITS.txt").readString()));

        UIElement returnButton = new UIElement(16.0F, -16.0F, 250.0F, 50.0F) {
            public void onClick() {
                super.onClick();
                GameState.switchToGameState(new MainMenu());
            }
        };
        returnButton.hAnchor = HorizontalAnchor.RIGHT_ALIGNED;
        returnButton.vAnchor = VerticalAnchor.BOTTOM_ALIGNED;
        returnButton.setText(Lang.get("Return_to_Main_Menu"));
        returnButton.show();

        this.uiObjects.add(returnButton);
    }

    float animationTime;
    float sizeY;

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
            Texture textLogo = MainMenu.textLogo;
            batch.setProjectionMatrix(this.uiCamera.combined);
            batch.begin();
            float y = -this.uiViewport.getWorldHeight() / 2.0F + 16.0F + yOff;
            float lineHeight = 24.0F;

            for (ICreditElement creditElement : categories) {

                if (creditElement instanceof TextureCreditElement category) {
                    float texScale = 1;

                    int texHeight = category.getTexture().getHeight();
                    int texWidth = category.getTexture().getWidth();

                    batch.draw(category.getTexture(), -texScale * texWidth / 2, y, 0, 0, texWidth / texScale, texHeight / texScale, texScale, texScale, 0, 0, 0, texWidth, texHeight, false, true);

                    y += texHeight - (texHeight / 4f);
                }
                if (creditElement instanceof CreditCategory category) {
                    String categoryTitle = "§0---- " + category.getTitle() + " §0----";

                    getTextDimensions(uiViewport, FormatText.FORMAT_PATTER.matcher(categoryTitle).replaceAll(""), v2);
                    FormattedTextRenderer.drawText(batch, uiViewport, categoryTitle,  -v2.x / 2, y);
//                    TextRenderer.drawText(categoryTitle, batch, uiViewport, null, -v2.x / 2, y, null, null);
                    y += lineHeight * 1.5f;
                    for (String name : category.getNames()) {
                        getTextDimensions(uiViewport, FormatText.FORMAT_PATTER.matcher(name).replaceAll(""), v2);
                        FormattedTextRenderer.drawText(batch, uiViewport, name,  -v2.x / 2, y);
//                        TextRenderer.drawText(name, batch, uiViewport, Color.WHITE.cpy(), -v2.x / 2, y, null, null);
                        y += lineHeight;
                    }
                }

                y += lineHeight * 2.5f;

            }

            this.animationTime += Gdx.graphics.getDeltaTime() * scrollSpeed;
            if (y < -this.uiViewport.getWorldHeight() / 2.0F) {
                this.animationTime = -this.uiViewport.getWorldHeight();
            }

            batch.end();
            this.drawUIElements();
        }
    }
}
