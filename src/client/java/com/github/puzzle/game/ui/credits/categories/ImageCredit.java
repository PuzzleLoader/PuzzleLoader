package com.github.puzzle.game.ui.credits.categories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.puzzle.core.Constants;
import com.github.puzzle.game.resources.PuzzleGameAssetLoader;
import org.hjson.JsonObject;
import org.hjson.JsonValue;

import java.util.Objects;
import java.util.function.Function;

public class ImageCredit implements ICreditElement {

    Texture texture;

    float scale = 1;
    float rotation = 0;

    int topPadding;
    int bottomPadding;

    public ImageCredit() {}

    public ImageCredit(Texture texture) {
        this.texture = texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setTopPadding(int topPadding) {
        this.topPadding = topPadding;
    }

    public void setBottomPadding(int bottomPadding) {
        this.bottomPadding = bottomPadding;
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport, Function<Float, Float> posModulator) {
        int texWidth = this.getTexture().getWidth();
        int texHeight = this.getTexture().getHeight();

        float scaledTexWidth = texWidth * this.getScale();
        float scaledTexHeight = texHeight * this.getScale();

        batch.draw(
                this.getTexture(),
                -scaledTexWidth / 2f,
                posModulator.apply(0f),
                0, 0,
                texWidth, texHeight,
                this.getScale(), this.getScale(),
                this.getRotation(),
                0, 0,
                texWidth, texHeight, false, true);
    }

    @Override
    public void fromJson(JsonObject object) {
        String src = object.getString("src", "puzzle-loader:icons/example.png");
        this.scale = object.getFloat("scale", 1);
        this.rotation = Objects.equals(src, "puzzle-loader:textures/logos/credits_logo.png") && Constants.isDev ? 75 : object.getFloat("rotation", 0);

        JsonValue value = object.get("padding");
        if (value != null) {
            JsonObject padding = value.asObject();

            this.topPadding = padding.getInt("top", 0);
            this.bottomPadding = padding.getInt("bottom", 0);
        }

        this.texture = PuzzleGameAssetLoader.LOADER.loadSync(src, Texture.class);
    }

    @Override
    public float getHeight() {
        return texture.getHeight() * scale;
    }

    public int getTopPadding() {
        return topPadding;
    }

    public int getBottomPadding() {
        return bottomPadding;
    }

    public float getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public Texture getTexture() {
        return texture;
    }

}
