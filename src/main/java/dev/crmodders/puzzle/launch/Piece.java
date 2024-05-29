package dev.crmodders.puzzle.launch;

import dev.crmodders.puzzle.mod.ModLocator;
import dev.crmodders.puzzle.providers.api.GameProviderScaffold;
import dev.crmodders.puzzle.providers.impl.CosmicReachProvider;
import dev.crmodders.puzzle.utils.MethodUtil;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Piece {
    public Class<? extends GameProviderScaffold> DEFAULT_PROVIDER = CosmicReachProvider.class;
    public GameProviderScaffold provider;

    public static Map<String, Object> blackboard = new HashMap<>();
    public static PuzzleClassLoader classLoader;

    public static Logger logger = LogManager.getLogger("Puzzle | Loader");

    public static void main(String[] args) {
        new Piece().launch(args);
    }

    private Piece() {
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
            provider = (GameProviderScaffold) Class.forName(DEFAULT_PROVIDER.getName(), true, classLoader).newInstance();

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