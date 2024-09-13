package com.github.puzzle.game.engine.rendering.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import finalforeach.cosmicreach.ui.FontRenderer;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;

import java.util.ArrayList;

import static finalforeach.cosmicreach.ui.FontRenderer.getTextDimensions;

public class TextRenderer {

   private static final Vector2 tmpTextDim = new Vector2();

    public static void drawText(String text, SpriteBatch batch, Viewport vp, int color,float x,float y){
        Color colour = new Color(color);
        drawText(text,batch,vp,colour,x,y);
    }
    public static void drawText(String text, SpriteBatch batch, Viewport vp, int color,float x,float y,HorizontalAnchor horizontalAnchor,VerticalAnchor verticalAnchor){
        Color colour = new Color(color);
        drawText(text,batch,vp,colour,x,y,horizontalAnchor,verticalAnchor);
    }
    public static void drawText(String text, SpriteBatch batch, Viewport vp, Color color, float x, float y){
        drawText(text,batch,vp,color,x,y, HorizontalAnchor.LEFT_ALIGNED, VerticalAnchor.BOTTOM_ALIGNED);
    }
    public static void drawText(String text, SpriteBatch batch, Viewport vp, Color color, float x, float y,HorizontalAnchor horizontalAnchor,VerticalAnchor verticalAnchor)
   {
        FormatText ft = FormatText.of(text);
        if(color != null) {
            batch.setColor(color);
            FontRenderer.drawText(batch,vp,ft.strip(),x,y);
            batch.setColor(Color.WHITE.cpy());
            return;
        }
        Color clr2 = batch.getColor().cpy();

        int index = 0;
        ArrayList<String > arr = new ArrayList<>(ft.parseIndex.keySet());
        for(int i = ft.parseIndex.keySet().size()-1; i >= 0; i--){
            String f = arr.get(i);
            if(f.isEmpty() || f.isBlank())
                continue;
            Color clr = ft.parseIndex.get(f) == null ? Color.WHITE.cpy() :  ft.parseIndex.get(f).cpy();
            batch.setColor(clr);
            FontRenderer.drawText(batch,vp,f,x + index ,y,horizontalAnchor,verticalAnchor);
            index += (int) getTextDimensions(vp, f, tmpTextDim).x;
        }
        batch.setColor(clr2);
    }
}
