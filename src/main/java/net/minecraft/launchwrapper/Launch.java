package net.minecraft.launchwrapper;

import dev.crmodders.puzzle.launch.Piece;
import dev.crmodders.puzzle.providers.api.GameProviderScaffold;
import dev.crmodders.puzzle.providers.impl.CosmicReachProvider;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import net.appel.main.Main;
import net.appel.tweakers.CosmicReachClientTweaker;
import org.apache.logging.log4j.Level;

public class Launch {
//    private static final String DEFAULT_TWEAK = CosmicReachClientTweaker.class.getName();
    private static final String DEFAULT_TWEAK = CosmicReachProvider.class.getName();
    public static Map<String, Object> blackboard;

    public static void main(String[] args) {
        new Launch().launch(args);
    }

    public static PuzzleClassLoader classLoader;

    private Launch() {
        //Get classpath
        List<URL> urls = new ArrayList<>();
        if (getClass().getClassLoader() instanceof URLClassLoader) {
            Collections.addAll(urls, ((URLClassLoader) getClass().getClassLoader()).getURLs());
        } else {
            for (String s : System.getProperty("java.class.path").split(File.pathSeparator)) {
                try {
                    urls.add(new File(s).toURI().toURL());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
        urls.addAll(Arrays.asList(Main.getUrls()));
        if (Piece.searchForCosmicReach() != null) {
            try {
                urls.add(Piece.searchForCosmicReach().file().toURL());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        classLoader = new PuzzleClassLoader(urls.toArray(new URL[0]));
        blackboard = new HashMap<>();
        Thread.currentThread().setContextClassLoader(classLoader);
    }

    private void launch(String[] args) {
        final OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();

        try {
            classLoader.addClassLoaderExclusion(DEFAULT_TWEAK.substring(0, DEFAULT_TWEAK.lastIndexOf('.')));
            GameProviderScaffold primaryProvider = (GameProviderScaffold) Class.forName(DEFAULT_TWEAK, true, classLoader).newInstance();

            primaryProvider.initArgs(args);
            primaryProvider.inject(classLoader);

            String[] argz = primaryProvider.getArgs().toArray(new String[0]);
            // Finally, we turn to the primary tweaker, and let it tell us where to go to launch
            final String launchTarget = primaryProvider.getEntrypoint();
            final Class<?> clazz = Class.forName(launchTarget, false, classLoader);
            final Method mainMethod = clazz.getMethod("main", String[].class);

            LogWrapper.info("Launching wrapped minecraft {%s}", launchTarget);
            mainMethod.invoke(null, (Object) argz);
        } catch (Exception e) {
            LogWrapper.log(Level.ERROR, e, "Unable to launch");
            System.exit(1);
        }
    }
}