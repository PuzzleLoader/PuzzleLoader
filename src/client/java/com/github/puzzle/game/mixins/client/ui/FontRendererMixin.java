package com.github.puzzle.game.mixins.client.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.puzzle.game.engine.rendering.text.FormattedTextRenderer;
import finalforeach.cosmicreach.ui.FontRenderer;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(FontRenderer.class)
public class FontRendererMixin {

    /**
     * @author Mr_Zombii
     * @reason Change the text drawer to use FormattedTextRenderer
     */
    @Overwrite
    public static void drawText(Batch batch, Viewport uiViewport, String text, float xStart, float yStart, HorizontalAnchor hAnchor, VerticalAnchor vAnchor) {
        FormattedTextRenderer.drawText(batch, uiViewport, text, xStart, yStart, hAnchor, vAnchor);
    }

}
