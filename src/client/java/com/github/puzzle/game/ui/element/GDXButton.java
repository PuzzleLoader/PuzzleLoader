package com.github.puzzle.game.ui.element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.util.function.Function;

public class GDXButton extends Table implements Disposable, IGDXElement {

    Label label;
    Image image;
    GDXButtonStyle style;

    boolean isChecked;
    boolean isDisabled;

    ClickListener listener;

    public GDXButton(
            String text, GDXButtonStyle style
    ) {
        this.setTouchable(Touchable.enabled);

        this.style = style;
        this.defaults().space(3.0F);
        listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isDisabled) {
                    isChecked = !isChecked;
                }
                onClick(event, x, y);
            }
        };
        addListener(listener);

        label = new Label(text, new Label.LabelStyle(style.font, style.fontColor));
        label.setAlignment(1);
        add(label);
        setStyle(style);
        setSize(getPrefWidth(), getPrefHeight());
    }

    public Image getImage() {
        return this.image;
    }

    public Cell<Image> getImageCell() {
        return this.getCell(this.image);
    }

    public void setLabel(Label label) {
        this.getLabelCell().setActor(label);
        this.label = label;
    }

    public Label getLabel() {
        return this.label;
    }

    public Cell<Label> getLabelCell() {
        return this.getCell(this.label);
    }

    public void setText(CharSequence text) {
        this.label.setText(text);
    }

    @Override
    public void dispose() {

    }

    public void setStyle(GDXButtonStyle style) {
        this.style = style;
        if (this.image != null) {
            this.updateBackground();
        }

        if (this.label != null) {
            Label.LabelStyle labelStyle = this.label.getStyle();
            labelStyle.font = style.font;
            labelStyle.fontColor = style.fontColor;
            this.label.setStyle(labelStyle);
        }
    }

    private void updateBackground() {
        image.setDrawable(getBackgroundDrawable());
    }

    protected Drawable getBackgroundDrawable() {
        if (this.isDisabled() && this.style.disabled != null) {
            return this.style.disabled;
        } else {
            if (this.isPressed()) {
                if (this.isChecked() && this.style.checkedDown != null) {
                    return this.style.checkedDown;
                }

                if (this.style.down != null) {
                    return this.style.down;
                }
            }

            if (this.isHoveringOver()) {
                if (this.isChecked()) {
                    if (this.style.checkedOver != null) {
                        return this.style.checkedOver;
                    }
                } else if (this.style.over != null) {
                    return this.style.over;
                }
            }

            boolean focused = this.hasKeyboardFocus();
            if (this.isChecked()) {
                if (focused && this.style.checkedFocused != null) {
                    return this.style.checkedFocused;
                }

                if (this.style.checked != null) {
                    return this.style.checked;
                }

                if (this.isHoveringOver() && this.style.over != null) {
                    return this.style.over;
                }
            }

            return focused && this.style.focused != null ? this.style.focused : this.style.up;
        }
    }

    public void superDraw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public void draw(Batch batch, float parentAlpha) {
        this.validate();
        this.setBackground(this.getBackgroundDrawable());
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

        super.draw(batch, parentAlpha);
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

    public boolean isDisabled() {
        return isDisabled;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public boolean isPressed() {
        return listener.isPressed();
    }

    public GDXButtonStyle getStyle() {
        return style;
    }

    public float getPrefWidth() {
        float width = super.getPrefWidth();
        if (this.style.up != null) {
            width = Math.max(width, this.style.up.getMinWidth());
        }

        if (this.style.down != null) {
            width = Math.max(width, this.style.down.getMinWidth());
        }

        if (this.style.checked != null) {
            width = Math.max(width, this.style.checked.getMinWidth());
        }

        return width;
    }

    public float getPrefHeight() {
        float height = super.getPrefHeight();
        if (this.style.up != null) {
            height = Math.max(height, this.style.up.getMinHeight());
        }

        if (this.style.down != null) {
            height = Math.max(height, this.style.down.getMinHeight());
        }

        if (this.style.checked != null) {
            height = Math.max(height, this.style.checked.getMinHeight());
        }

        return height;
    }

    public float getMinWidth() {
        return this.getPrefWidth();
    }

    public float getMinHeight() {
        return this.getPrefHeight();
    }

    @Override
    public void onClick(InputEvent event, float mouseX, float mouseY) {
    }

    @Override
    public void onHover(float deltaTime, float mouseX, float mouseY) {

    }

    @Override
    public boolean isHoveringOver() {
        return listener.isOver();
    }

    public CharSequence getText() {
        return label.getText();
    }

    public static class GDXButtonStyle {

        public BitmapFont font;
        public Color fontColor;

        public Drawable up;
        public Drawable down;
        public Drawable over;
        public Drawable focused;
        public Drawable disabled;
        public Drawable checked;
        public Drawable checkedOver;
        public Drawable checkedDown;
        public Drawable checkedFocused;

        public float pressedOffsetX;
        public float pressedOffsetY;
        public float unpressedOffsetX;
        public float unpressedOffsetY;
        public float checkedOffsetX;
        public float checkedOffsetY;

        public GDXButtonStyle() {}

        public GDXButtonStyle(Function<GDXButtonStyle, GDXButtonStyle> modify) {
            modify.apply(this);
        }

        public Button.ButtonStyle toButtonStyle() {
            Button.ButtonStyle style = new Button.ButtonStyle();
            style.up = up;
            style.down = down;
            style.over = over;
            style.focused = focused;
            style.disabled = disabled;
            style.checked = checked;
            style.checkedOver = checkedOver;
            style.checkedDown = checkedDown;
            style.checkedFocused = checkedFocused;
            style.pressedOffsetX = pressedOffsetX;
            style.pressedOffsetY = pressedOffsetY;
            style.unpressedOffsetX = unpressedOffsetX;
            style.unpressedOffsetY = unpressedOffsetY;
            style.checkedOffsetX = checkedOffsetX;
            style.checkedOffsetY = checkedOffsetY;
            return style;
        }
    }
}
