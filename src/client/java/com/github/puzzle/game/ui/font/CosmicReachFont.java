package com.github.puzzle.game.ui.font;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import finalforeach.cosmicreach.ui.FontRenderer;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CosmicReachFont {

    public static final BitmapFont FONT = createCosmicReachFont();
    public static final BitmapFont FONT_BIG = createCosmicReachFont();

    static {
        FONT_BIG.getData().setScale(2.5F);
    }

    public static BitmapFont createCosmicReachFont() {
        List<FontTexture> fontTextures = new ArrayList<>();

        try {
            Class<?> fontTextureType = Class.forName("finalforeach.cosmicreach.ui.FontTexture");
            for(Field field : FontRenderer.class.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(null);
                if(fontTextureType.isInstance(value)) {
                    FontTexture tex = new FontTexture(value, fontTextureType);
                    fontTextures.add(tex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int totalSizeInSquarePixels = fontTextures.size() * 256 * 256;
        int width;
        int height = -1;
        for(width = 256; width <= 16384; width += 256) {
            height = totalSizeInSquarePixels / width;
            if(width >= height && Math.floorDiv(height, 256) * 256 == height) {
                break;
            }
        }

        int x = 0;
        int y = 0;

        Map<FontTexture, Point> locations = new HashMap<>();
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        for(FontTexture ft : fontTextures) {
            TextureData td = ft.fontTextureRegions[0].getTexture().getTextureData();
            td.prepare();
            pixmap.drawPixmap(td.consumePixmap(), x, y);
            locations.put(ft, new Point(x, y));
            x += 256;
            if(x >= width) {
                x = 0;
                y += 256;
            }
        }

        Texture texture = new Texture(pixmap);
        BitmapFont.BitmapFontData data = new BitmapFont.BitmapFontData();
        for(FontTexture ft : fontTextures) {
            int xOffset = locations.get(ft).x;
            int yOffset = locations.get(ft).y;
            for(int unicode = ft.unicodeStart; unicode < ft.unicodeStart + 256; unicode++) {
                Vector2 charStart = ft.fontCharStartPos[unicode - ft.unicodeStart];
                Vector2 charSize = ft.fontCharSizes[unicode - ft.unicodeStart];
                TextureRegion textureRegion = ft.fontTextureRegions[unicode - ft.unicodeStart];
                BitmapFont.Glyph glyph = new BitmapFont.Glyph();
                glyph.id = unicode;
                glyph.srcX = xOffset + (int) charStart.x;
                glyph.srcY = yOffset + textureRegion.getRegionY() - textureRegion.getRegionHeight();
                glyph.width = (int) charSize.x;
                glyph.height = textureRegion.getRegionHeight();
                glyph.xadvance = glyph.width + 2;
                data.setGlyph(unicode, glyph);
            }
        }
        data.down = -data.getGlyph('\n').height;
        BitmapFont.Glyph space = data.getGlyph(' ');
        space.width /= 4;
        space.xadvance = space.width;
        return new BitmapFont(data, new TextureRegion(texture), true);
    }

}
