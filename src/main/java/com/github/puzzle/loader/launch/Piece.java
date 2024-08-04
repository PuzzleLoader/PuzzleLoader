package com.github.puzzle.loader.launch;

import com.github.puzzle.game.provider.CosmicReachProvider;
import com.github.puzzle.loader.mod.ModLocator;
import com.github.puzzle.loader.providers.api.IGameProvider;
import com.github.puzzle.util.MethodUtil;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class Piece {
    public String DEFAULT_PROVIDER = CosmicReachProvider.class.getName();
    public static IGameProvider provider;

    public static Map<String, Object> blackboard;
    public static PuzzleClassLoader classLoader;

    public static final Logger LOGGER = LogManager.getLogger("Puzzle | Loader");

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
            OptionSpec<String> provider_option = parser.accepts("gameProvider").withOptionalArg().ofType(String.class);
            OptionSpec<String> modFolder_option = parser.accepts("modFolder").withOptionalArg().ofType(String.class);

            classLoader.addClassLoaderExclusion(DEFAULT_PROVIDER.substring(0, DEFAULT_PROVIDER.lastIndexOf('.')));
            classLoader.addClassLoaderExclusion("com.github.puzzle.annotations");
//            classLoader.addClassLoaderExclusion("com.github.puzzle.loader.entrypoint");
            classLoader.addClassLoaderExclusion("com.github.puzzle.loader.launch");
            classLoader.addClassLoaderExclusion("com.github.puzzle.loader.mod");
            classLoader.addClassLoaderExclusion("com.github.puzzle.loader.providers");
            classLoader.addClassLoaderExclusion("com.github.puzzle.utils");

            if (options.has(provider_option))
                provider = (IGameProvider) Class.forName(provider_option.value(options), true, classLoader).newInstance();
            else
                provider = (IGameProvider) Class.forName(DEFAULT_PROVIDER, true, classLoader).newInstance();



            provider.registerTransformers(classLoader);
            provider.initArgs(args);
            provider.inject(classLoader);

            String[] providerArgs = provider.getArgs().toArray(new String[0]);

            Class<?> clazz = Class.forName(provider.getEntrypoint(), false, classLoader);
            Method main = MethodUtil.getMethod(clazz,"main", String[].class);
            LOGGER.info("Launching {} version {}", provider.getName(), provider.getRawVersion());
            MethodUtil.runStaticMethod(main, (Object) providerArgs);
        } catch (Exception e) {
            LOGGER.error("Unable To Launch", e);
            System.exit(1);
        }
    }
}