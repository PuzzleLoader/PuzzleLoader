package com.github.puzzle.game.ui.credits.categories;

import com.badlogic.gdx.graphics.Texture;

public class TextureCreditElement implements ICreditElement {

    Texture texture;

    public TextureCreditElement(Texture texture) {
        this.texture = texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }

}
