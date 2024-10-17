package com.github.puzzle.game.engine.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class TextureFaker {

    public static Random random = new Random();

    public static final Texture textureFaker(int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int nextInt = random.nextInt(256*256*256);
                pixmap.drawPixel(x, y, Color.valueOf(String.format("%06x", nextInt)).toIntBits());
            }
        }
        return new Texture(pixmap);
    }

    public static final Texture fakerColor(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixmap.drawPixel(x, y, color.toIntBits());
            }
        }
        return new Texture(pixmap);
    }

    public static final Color randomColor() {
        return Color.valueOf(String.format("%06x", random.nextInt(256*256*256)));
    }

    public static int toIntBits(Color color) {
        int alpha = (int) (color.a * 255);
        int red = (int) (color.r * 255);
        int green = (int) (color.g * 255);
        int blue = (int) (color.b * 255);

        return red << 24 | green << 16 | blue << 8 | alpha;
    }

    public static Texture generateBorderedTexture(Color backGround, Color border, int borderWidth, int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        int borderColor = toIntBits(border);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Background
                pixmap.drawPixel(x, y, toIntBits(backGround));

                // Top Border
                if (y < borderWidth) pixmap.drawPixel(x, y, borderColor);
                // Bottom Border
                if (y >= height - borderWidth) pixmap.drawPixel(x, y, borderColor);
                // Left Border
                if (x < borderWidth) pixmap.drawPixel(x, y, borderColor);
                // Right Border
                if (x >= width - borderWidth) pixmap.drawPixel(x, y, borderColor);
            }
        }
        return new Texture(pixmap);
    }
}
