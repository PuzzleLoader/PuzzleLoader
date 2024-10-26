package com.github.puzzle.game.ui.menus.credits;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.puzzle.rendering.ui.font.FormatText;
import com.github.puzzle.rendering.ui.font.FormattedTextRenderer;
import org.hjson.JsonArray;
import org.hjson.JsonObject;
import org.hjson.JsonValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static finalforeach.cosmicreach.ui.FontRenderer.getTextDimensions;

public class ListCredit implements ICreditElement {

    String title;
    List<String> names;

    public ListCredit() {}

    public ListCredit(String name) {
        this.title = name;
        this.names = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void addName(String name) {
        names.add(name);
    }

    public void addName(String ...namez) {
        names.addAll(Arrays.asList(namez));
    }

    public void addNames(String[] namez) {
        names.addAll(Arrays.asList(namez));
    }

    public List<String> getNames() {
        return names;
    }

    private static final float lineHeight = 24.0F;
    private static final Vector2 v2 = new Vector2();

    @Override
    public void render(SpriteBatch batch, Viewport viewport, Function<Float, Float> posModulator) {
        String categoryTitle = "§f---- " + getTitle() + " §f----";

        getTextDimensions(viewport, FormatText.FORMAT_PATTER.matcher(categoryTitle).replaceAll(""), v2);
        FormattedTextRenderer.drawText(batch, viewport, categoryTitle,  -v2.x / 2, posModulator.apply(0f));
        posModulator.apply(lineHeight * 1.5f);

        for (String name : getNames()) {
            getTextDimensions(viewport, FormatText.FORMAT_PATTER.matcher(name).replaceAll(""), v2);
            FormattedTextRenderer.drawText(batch, viewport, name,  -v2.x / 2, posModulator.apply(0f));
            posModulator.apply(lineHeight);
        }

        posModulator.apply(lineHeight * 1.5f);
    }

    @Override
    public void fromJson(JsonObject object) {
        this.title = object.getString("title", "§dY§[fe60fe]O§[fe82fe]U §[fea4fe]F§[fec6fe]O§[fee8fe]R§[fef3fe]G§[fed1fe]O§[feaffe]T §[fe8efe]A §[fe6cfe]T§[fe4afe]I§[fe55fe]T§[fe77fe]L§[fe99fe]E §[febbfe]>§[feddfe]:§f(");
        this.names = new ArrayList<>();

        JsonValue nameArray = object.get("names");
        if (nameArray != null) {
            JsonArray array = nameArray.asArray();

            for (JsonValue value : array) {
                this.names.add(value.asString());
            }
        }
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public int getTopPadding() {
        return 0;
    }

    @Override
    public int getBottomPadding() {
        return 0;
    }
}