package dev.crmodders.puzzle.game.generators;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import dev.crmodders.puzzle.core.Identifier;

public class SingleColorModelGenerator extends BlockModelGenerator {
    public SingleColorModelGenerator(Identifier blockId, String modelName, Color color) {
        super(blockId, modelName);
        Pixmap texture = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        texture.setColor(color);
        texture.fill();
        createTexture("all", texture);
        createCuboid(0, 0, 0, 16, 16, 16, "all");
    }
}
