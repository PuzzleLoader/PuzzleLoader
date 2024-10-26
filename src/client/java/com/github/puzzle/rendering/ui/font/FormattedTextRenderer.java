package com.github.puzzle.rendering.ui.font;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import finalforeach.cosmicreach.ui.FontRenderer;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormattedTextRenderer {

    static final Map<String, FormatText> textCache = new HashMap<>();

    public static void drawText(Batch batch, Viewport vp, String text, float xStart, float yStart) {
        drawText(batch, vp, text, xStart, yStart, false);
    }

    public static void drawText(SpriteBatch batch, Viewport uiViewport, String text, float xStart, float yStart, HorizontalAnchor hAnchor, VerticalAnchor vAnchor) {
        Color resetColor = batch.getColor().cpy();

        FormatText formatted = textCache.get(text);
        formatted = formatted == null ? FormatText.of(text, resetColor) : formatted;

        drawText(batch, uiViewport, formatted, xStart, yStart, hAnchor, vAnchor);
    }

    public static void drawText(Batch batch, Viewport vp, String text, float xStart, float yStart, boolean flip) {
        Color resetColor = batch.getColor().cpy();

        FormatText formatted = textCache.get(text);
        formatted = formatted == null ? FormatText.of(text, resetColor) : formatted;

        drawText(batch, vp, formatted, xStart, yStart, flip);
    }

    public static void drawText(Batch batch, Viewport vp, FormatText text, float xStart, float yStart) {
        drawText(batch, vp, text, xStart, yStart, false);
    }

    public static void drawText(SpriteBatch batch, Viewport uiViewport, FormatText text, float xStart, float yStart, HorizontalAnchor hAnchor, VerticalAnchor vAnchor) {
        Vector2 textDim = FontRenderer.getTextDimensions(uiViewport, text.getText(), new Vector2());
        float w = textDim.x;
        float h = textDim.y;
        switch (hAnchor) {
            case LEFT_ALIGNED:
                xStart -= uiViewport.getWorldWidth() / 2.0F;
                break;
            case RIGHT_ALIGNED:
                xStart = xStart + uiViewport.getWorldWidth() / 2.0F - w;
                break;
            case CENTERED:
            default:
                xStart -= w / 2.0F;
        }

        switch (vAnchor) {
            case TOP_ALIGNED:
                yStart -= uiViewport.getWorldHeight() / 2.0F;
                break;
            case BOTTOM_ALIGNED:
                yStart = yStart + uiViewport.getWorldHeight() / 2.0F - h;
                break;
            case CENTERED:
            default:
                yStart -= h / 2.0F;
        }

        drawText(batch, uiViewport, text, xStart, yStart);
    }

    public static void drawText(Batch batch, Viewport vp, FormatText text, float xStart, float yStart, boolean flip) {
        Color resetColor = batch.getColor().cpy();

        float totalSize = 0;
        List<FormatText.TextPart> parts = text.getParts();

        for (FormatText.TextPart part : parts) {
            batch.setColor(part.color());
            FontRenderer.drawText(batch, vp, part.text(), xStart + totalSize, yStart, flip);
            totalSize += part.getWidth(vp);
        }
        batch.setColor(resetColor);
    }

}