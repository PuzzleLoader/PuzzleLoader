package dev.crmodders.puzzle.core.launch;

import dev.crmodders.puzzle.core.mod.ModLocator;
import dev.crmodders.puzzle.core.providers.api.GameProvider;
import dev.crmodders.puzzle.core.providers.impl.CosmicReachProvider;
import dev.crmodders.puzzle.utils.MethodUtil;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.util.JavaVersion;
import org.spongepowered.asm.util.asm.ASM;

public class Piece {
    public Class<? extends GameProvider> DEFAULT_PROVIDER = CosmicReachProvider.class;
    public static GameProvider provider;

    public static Map<String, Object> blackboard;
    public static PuzzleClassLoader classLoader;

    public static final Logger logger = LogManager.getLogger("Puzzle | Loader");

    public static void main(String[] args) {
        new Piece().launch(args);
    }

    public static LAUNCH_STATE MOD_LAUNCH_STATE;

    public enum LAUNCH_STATE {
        PRE_MIXIN_INJECT,
        PRE_INIT,
        INIT,
        IN_GAME
    }

    private Piece() {
        // "Hack" asm to get it to not yell at me (:
        try {
            ASM.getApiVersionString();

            Field majorVersion = ASM.class.getDeclaredField("majorVersion");
            majorVersion.setAccessible(true);
            majorVersion.set(null, 9);

            Field minorVersion = ASM.class.getDeclaredField("minorVersion");
            minorVersion.setAccessible(true);
            minorVersion.set(null, 7);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        List<URL> classPath = new ArrayList<>();

        classPath.addAll(ModLocator.getUrlsOnClasspath());
        ModLocator.crawlModsFolder(classPath);

        classLoader = new PuzzleClassLoader(classPath);
        blackboard = new HashMap<>();
        Thread.currentThread().setContextClassLoader(classLoader);
    }

    private void launch(String[] args) {
        final OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();

        final OptionSet options = parser.parse(args);
        try {
            classLoader.addClassLoaderExclusion(DEFAULT_PROVIDER.getName().substring(0, DEFAULT_PROVIDER.getName().lastIndexOf('.')));
            classLoader.addClassLoaderExclusion("dev.crmodders.puzzle.annotations");
            classLoader.addClassLoaderExclusion("dev.crmodders.puzzle.core.entrypoint");
            classLoader.addClassLoaderExclusion("dev.crmodders.puzzle.core.launch");
            classLoader.addClassLoaderExclusion("dev.crmodders.puzzle.core.mod");
            classLoader.addClassLoaderExclusion("dev.crmodders.puzzle.core.providers");
            classLoader.addClassLoaderExclusion("dev.crmodders.puzzle.core.tags");
            classLoader.addClassLoaderExclusion("dev.crmodders.puzzle.utils");
            provider = (GameProvider) Class.forName(DEFAULT_PROVIDER.getName(), true, classLoader).newInstance();

            provider.initArgs(args);
            provider.inject(classLoader);

            String[] providerArgs = provider.getArgs().toArray(new String[0]);

            Class<?> clazz = Class.forName(provider.getEntrypoint(), false, classLoader);
            Method main = MethodUtil.getMethod(clazz,"main", String[].class);
            logger.info("Launching {} version {}", provider.getName(), provider.getRawVersion());
            MethodUtil.runStaticMethod(main, (Object) providerArgs);
        } catch (Exception e) {
            logger.error("Unable To Launch", e);
            System.exit(1);
        }
    }
}