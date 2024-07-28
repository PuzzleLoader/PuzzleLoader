package dev.crmodders.puzzle.game;

import dev.crmodders.puzzle.game.serialization.api.IPuzzleBinaryDeserializer;
import dev.crmodders.puzzle.game.serialization.api.IPuzzleBinarySerializer;
import dev.crmodders.puzzle.game.serialization.impl.PuzzleCRBinDeserializer;
import dev.crmodders.puzzle.game.serialization.impl.PuzzleCRBinSerializer;
import finalforeach.cosmicreach.rendering.IZoneRenderer;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Globals {
    public static List<IZoneRenderer> renderers = new ArrayList<>();
    public static int rendererIndex = 0;

    public static Class<? extends IPuzzleBinarySerializer> defaultSerializer;
    public static Class<? extends IPuzzleBinaryDeserializer> defaultDeserializer;

    public static void initRenderers() {
        Logger LOGGER = LoggerFactory.getLogger("Puzzle | Renderers");

        Reflections ref = new Reflections();

        Set<Class<? extends IZoneRenderer>> classes = ref.getSubTypesOf(IZoneRenderer.class);
        LOGGER.warn("Getting renderers");

        for (Class<? extends IZoneRenderer> c : classes) {
            try {
                LOGGER.warn("\t{}",c.getName());
                renderers.add(c.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                 LOGGER.warn("Can't use class \"{}\" as renderer",c.getName());
            }
        }

    }
}
