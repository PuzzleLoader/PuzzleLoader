package com.github.puzzle.game;

import com.github.puzzle.core.Puzzle;
import com.github.puzzle.core.localization.Language;
import finalforeach.cosmicreach.rendering.IZoneRenderer;
import finalforeach.cosmicreach.settings.BooleanSetting;
import finalforeach.cosmicreach.settings.IntSetting;
import finalforeach.cosmicreach.util.Identifier;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Globals {
    public static List<IZoneRenderer> renderers = new ArrayList<>();
    public static Map<String,Integer> rendererIndexMap = new HashMap<>();
    public static int rendererIndex = 0;

    public static boolean GameLoaderHasLoaded;

    public static final Identifier LanguageEnUs = Identifier.of(Puzzle.MOD_ID, "languages/en-US.json");
    public static final IntSetting AntiAliasing = new IntSetting("msaa", 4);
    public static final BooleanSetting EnabledVanillaMods = new BooleanSetting("enableVanillaMods", true);
    public static Language SelectedLanguage;

    public static void initRenderers() {
        Logger LOGGER = LoggerFactory.getLogger("Puzzle | Renderers");

        Reflections ref = new Reflections();

        Set<Class<? extends IZoneRenderer>> classes = ref.getSubTypesOf(IZoneRenderer.class);
        LOGGER.warn("Getting renderers");

        for (Class<? extends IZoneRenderer> c : classes) {
            try {
                LOGGER.warn("\t{}",c.getName());
                int currrentIndex = renderers.size();
                renderers.add(c.getDeclaredConstructor().newInstance());
                rendererIndexMap.put(c.getName(),currrentIndex);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                 LOGGER.warn("Can't use class \"{}\" as renderer",c.getName());
            }

        }

    }
}
