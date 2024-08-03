package io.github.puzzle.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.lang.reflect.Field;

public class FontTexture {

    public int unicodeStart;
    public Texture fontTexture;
    public TextureRegion[] fontTextureRegions;
    public Vector2[] fontCharStartPos;
    public Vector2[] fontCharSizes;

    public FontTexture(Object fontTexture, Class<?> fontTextureClass) {
        try {
            Field field;
            field = fontTextureClass.getDeclaredField("unicodeStart");
            field.setAccessible(true);
            this.unicodeStart = field.getInt(fontTexture);

            field = fontTextureClass.getDeclaredField("fontTexture");
            field.setAccessible(true);
            this.fontTexture = (Texture) field.get(fontTexture);

            field = fontTextureClass.getDeclaredField("fontTextureRegions");
            field.setAccessible(true);
            this.fontTextureRegions = (TextureRegion[]) field.get(fontTexture);

            field = fontTextureClass.getDeclaredField("fontCharStartPos");
            field.setAccessible(true);
            this.fontCharStartPos = (Vector2[]) field.get(fontTexture);

            field = fontTextureClass.getDeclaredField("fontCharSizes");
            field.setAccessible(true);
            this.fontCharSizes = (Vector2[]) field.get(fontTexture);
        } catch (Exception e)  {
            throw new RuntimeException(e);
        }
    }

}
