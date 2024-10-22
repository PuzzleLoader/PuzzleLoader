package com.github.puzzle.core.loader.launch;

import com.github.puzzle.core.loader.meta.EnvType;
import com.github.puzzle.core.loader.provider.IGameProvider;
import com.github.puzzle.core.loader.util.MethodUtil;
import com.github.puzzle.core.loader.util.ModLocator;
import com.github.puzzle.core.loader.util.Reflection;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("deprecation")
public class Piece {
    public static String CLIENT_PROVIDER = "com.github.puzzle.game.excluded.ClientCRProvider";
    public static String SERVER_PROVIDER = "com.github.puzzle.game.excluded.ServerCRProvider";

    public static String DEFAULT_PROVIDER;
    public static IGameProvider provider;

    public static Map<String, Object> blackboard;
    public static PuzzleClassLoader classLoader;

    static AtomicReference<EnvType> env = new AtomicReference<>();

    public static final Logger LOGGER = LogManager.getLogger("Puzzle | Loader");

    public static void launch(String[] args, EnvType type) {
        Piece piece = new Piece();
        env.set(type);
        DEFAULT_PROVIDER = env.get() == EnvType.CLIENT ? CLIENT_PROVIDER : SERVER_PROVIDER;
        piece.launch(args);
    }

    private Piece() {
        if (classLoader != null) throw new RuntimeException("MORE THAN ONE PIECE CANNOT EXIST AT THE SAME TIME.");
        List<URL> classPath = new ArrayList<>();

        classPath.addAll(ModLocator.getUrlsOnClasspath());
        ModLocator.crawlModsFolder(classPath);

        classLoader = new PuzzleClassLoader(classPath);
        blackboard = new HashMap<>();
        Thread.currentThread().setContextClassLoader(classLoader);
    }

    public static void main(String[] args) {
        Piece piece = new Piece();
        DEFAULT_PROVIDER = getSide() == EnvType.CLIENT ? CLIENT_PROVIDER : SERVER_PROVIDER;
        piece.launch(args);
    }

    public static EnvType getSide() {
        if (env.get() != null) return env.get();

        try {
            Class.forName("finalforeach.cosmicreach.ClientSingletons");
        } catch (ClassNotFoundException e) {
            env.set(EnvType.SERVER);;
        }
        env.set(EnvType.CLIENT);;
        return env.get();
    }

    private void launch(String[] args) {
        final OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();

        final OptionSet options = parser.parse(args);
        try {
            OptionSpec<String> provider_option = parser.accepts("gameProvider").withOptionalArg().ofType(String.class);
            OptionSpec<String> modFolder_option = parser.accepts("modFolder").withOptionalArg().ofType(String.class);

            if (options.has(modFolder_option)) ModLocator.setModFolder(new File(modFolder_option.value(options)));
            classLoader.addClassLoaderExclusion(DEFAULT_PROVIDER.substring(0, DEFAULT_PROVIDER.lastIndexOf('.')));
            classLoader.addClassLoaderExclusion("com.github.puzzle.core.loader.launch");
            classLoader.addClassLoaderExclusion("com.github.puzzle.game.excluded.");
            classLoader.addClassLoaderExclusion("com.github.puzzle.core.loader.meta");
            classLoader.addClassLoaderExclusion("com.github.puzzle.core.loader.provider");
            classLoader.addClassLoaderExclusion("com.github.puzzle.core.loader.transformers");
            classLoader.addClassLoaderExclusion("com.github.puzzle.core.loader.util");

            if (options.has(provider_option))
                provider = (IGameProvider) Class.forName(provider_option.value(options), true, classLoader).newInstance();
            else
                provider = (IGameProvider) Class.forName(DEFAULT_PROVIDER, true, classLoader).newInstance();

            provider.registerTransformers(classLoader);
            provider.initArgs(args);
            provider.inject(classLoader);

            String[] providerArgs = provider.getArgs().toArray(new String[0]);

            Class<?> clazz = Class.forName(provider.getEntrypoint(), false, classLoader);
            Method main = Reflection.getMethod(clazz,"main", String[].class);
            LOGGER.info("Launching {} version {}", provider.getName(), provider.getRawVersion());
            MethodUtil.runStaticMethod(main, (Object) providerArgs);
        } catch (Exception e) {
            LOGGER.error("Unable To Launch", e);
            System.exit(1);
        }
    }
}