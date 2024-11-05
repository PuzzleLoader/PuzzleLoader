package com.github.puzzle.game.ui.element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.github.puzzle.game.ui.font.CosmicReachDefaultFont;
import de.pottgames.tuningfork.SoundBuffer;
import finalforeach.cosmicreach.GameAssetLoader;

public class CommonButton extends GDXButton {

    public static SoundBuffer onHoverSound;
    public static SoundBuffer onClickSound;

    public static Texture uiPanelTex = new Texture(GameAssetLoader.loadAsset("textures/ui-panel.png"));
    public static Texture uiPanelBoundsTex = new Texture(GameAssetLoader.loadAsset("textures/ui-panel-boundary.png"));
    public static Texture uiPanelHoverBoundsTex = new Texture(GameAssetLoader.loadAsset("textures/ui-panel-hover-boundary.png"));
    public static Texture uiPanelPressedTex = new Texture(GameAssetLoader.loadAsset("textures/ui-panel-pressed.png"));

    public CommonButton(
            float x, float y, float w, float h, String text
    ) {
        super(text, new GDXButtonStyle((s) -> {
            s.font = CosmicReachDefaultFont.FONT;
            s.fontColor = Color.WHITE.cpy();
            return s;
        }));
        setSize(w, h);
        setPosition(x, y);

        label.layout();
        style.pressedOffsetY = -((label.getGlyphLayout().height * label.getStyle().font.getLineHeight()) / 2f);
        style.unpressedOffsetY = -((label.getGlyphLayout().height * label.getStyle().font.getLineHeight()) / 2f);

        label.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                label.layout();
                style.pressedOffsetY = -((label.getGlyphLayout().height * label.getStyle().font.getLineHeight()) / 2f);
                style.unpressedOffsetY = -((label.getGlyphLayout().height * label.getStyle().font.getLineHeight()) / 2f);
            }
        });
        setStyle(style);

        addAction(new Action() {
            boolean isHovered = false;

            @Override
            public boolean act(float v) {
                if (CommonButton.this.isHoveringOver()) {
                    if (!isHovered) {
                        CommonButton.this.onHover(v, Gdx.input.getX(), Gdx.input.getY());
                    }
                    isHovered = true;
                } else {
                    isHovered = false;
                }
                return false;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        validate();
        float x = getX();
        float y = getY();

        Texture currentTex = !listener.isVisualPressed() ? uiPanelTex : uiPanelPressedTex;
        Texture currentBoundaryTex = !listener.isVisualPressed() && !this.isHoveringOver() ? uiPanelBoundsTex : uiPanelHoverBoundsTex;

        batch.draw(currentBoundaryTex, x, y, 0.0F, 0.0F, getWidth(), getHeight(), 1.0F, 1.0F, 0.0F, 0, 0, currentTex.getWidth(), currentTex.getHeight(), false, true);
        batch.draw(currentTex, x + 1.0F, y + 1.0F, 1.0F, 1.0F, getWidth() - 2.0F, getHeight() - 2.0F, 1.0F, 1.0F, 0.0F, 0, 0, currentTex.getWidth(), currentTex.getHeight(), false, true);

        float offsetX;
        float offsetY;
        if (this.isPressed() && !this.isDisabled()) {
            offsetX = this.style.pressedOffsetX;
            offsetY = this.style.pressedOffsetY;
        } else if (this.isChecked() && !this.isDisabled()) {
            offsetX = this.style.checkedOffsetX;
            offsetY = this.style.checkedOffsetY;
        } else {
            offsetX = this.style.unpressedOffsetX;
            offsetY = this.style.unpressedOffsetY;
        }

        boolean offset = offsetX != 0.0F || offsetY != 0.0F;
        Array<Actor> children = this.getChildren();
        int i;
        if (offset) {
            for(i = 0; i < children.size; ++i) {
                ((Actor)((Array)children).get(i)).moveBy(offsetX, offsetY);
            }
        }

        super.superDraw(batch, parentAlpha);
        if (offset) {
            for(i = 0; i < children.size; ++i) {
                ((Actor)((Array)children).get(i)).moveBy(-offsetX, -offsetY);
            }
        }

        Stage stage = this.getStage();
        if (stage != null && stage.getActionsRequestRendering() && this.isPressed() != this.listener.isPressed()) {
            Gdx.graphics.requestRendering();
        }
    }

    public void setFontColor(Color color) {
        this.style.fontColor = color.cpy();
    }

    public void updateText() {
    }

    @Override
    public void onClick(InputEvent event, float mouseX, float mouseY) {
        super.onClick(event, mouseX, mouseY);
        onClickSound.play();
    }

    @Override
    public void onHover(float deltaTime, float mouseX, float mouseY) {
        super.onHover(deltaTime, mouseX, mouseY);
        onHoverSound.play();
    }

    static {
        onHoverSound = GameAssetLoader.getSound("base:sounds/ui/e-button-hover.ogg");
        onClickSound = GameAssetLoader.getSound("base:sounds/ui/e-button-click.ogg");
    }
}
