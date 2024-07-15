package dev.crmodders.puzzle.core;

import com.google.common.reflect.Reflection;
import dev.crmodders.puzzle.core.launch.Piece;
import dev.crmodders.puzzle.core.mod.ModLocator;
import dev.crmodders.puzzle.core.rendering.PuzzleZoneRenderer;
import finalforeach.cosmicreach.rendering.IZoneRenderer;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Globals {

    public static List<IZoneRenderer> renderers = new ArrayList<>();
    public static int rendererIndex=0;

    public static void initRenderers() {
        Logger LOGGER =  LoggerFactory.getLogger("Puzzle | Renderers");

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
