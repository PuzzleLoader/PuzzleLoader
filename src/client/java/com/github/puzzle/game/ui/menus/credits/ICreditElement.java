package com.github.puzzle.game.ui.menus.credits;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.hjson.JsonObject;

import java.util.HashMap;
import java.util.function.Function;

public interface ICreditElement {

    HashMap<String, Class<? extends ICreditElement>> TYPE_TO_ELEMENT = new HashMap<>();

    void render(SpriteBatch batch, Viewport viewport, Function<Float, Float> posModulator);
    void fromJson(JsonObject object);

    float getHeight();

    int getTopPadding();
    int getBottomPadding();

}