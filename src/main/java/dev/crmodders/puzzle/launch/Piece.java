package dev.crmodders.puzzle.launch;

import dev.crmodders.puzzle.providers.api.GameProviderScaffold;
import dev.crmodders.puzzle.utils.MethodUtil;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import dev.crmodders.puzzle.mod.ModLocator;
import net.appel.tweakers.CosmicReachClientTweaker;
import net.minecraft.launchwrapper.PuzzleClassLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

public class Piece {

    public static Map<String, Object> blackboard = new HashMap<>();
    public static PuzzleClassLoader classLoader;

    public GameProviderScaffold provider;
    public Class<? extends GameProviderScaffold> providerClass = CosmicReachClientTweaker.class;

    public static Logger logger = LogManager.getLogger("Puzzle | Loader");

    public static String DEFAULT_MAIN_CLASS = "finalforeach.cosmicreach.lwjgl3.Lwjgl3Launcher";
    public static String DEFAULT_PACKAGE = "finalforeach.cosmicreach.lwjgl3";
    public static String GAME_MAIN_CLASS = DEFAULT_MAIN_CLASS;

    static File lookForJarVariations(String offs) {
        Pattern type1 = Pattern.compile("Cosmic Reach-[\\d]+\\.[\\d]+.[\\d]+\\.jar", Pattern.CASE_INSENSITIVE);
        Pattern type2 = Pattern.compile("Cosmic_Reach-[\\d]+\\.[\\d]+.[\\d]+\\.jar", Pattern.CASE_INSENSITIVE);
        Pattern type3 = Pattern.compile("CosmicReach-[\\d]+\\.[\\d]+.[\\d]+\\.jar", Pattern.CASE_INSENSITIVE);
        for (File f : Objects.requireNonNull(new File(offs).listFiles())) {
            if (type1.matcher(f.getName()).find()) return f;
            if (type2.matcher(f.getName()).find()) return f;
            if (type3.matcher(f.getName()).find()) return f;
            if (f.getName().equals("cosmic_reach.jar")) return f;
            if (f.getName().equals("cosmicreach.jar")) return f;
            if (f.getName().equals("cosmicReach.jar")) return f;
        }
        return null;
    }

    static FileJarPair toCrJar(File f) {
        try {
            JarFile crJar = new JarFile(f);
            GAME_MAIN_CLASS = crJar.getManifest().getMainAttributes().getValue("Main-Class");
            return new FileJarPair(crJar, f);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static FileJarPair searchForCosmicReach() {
        if (ClassLoader.getPlatformClassLoader().getDefinedPackage(DEFAULT_PACKAGE) == null) {File jarFile;
            jarFile = lookForJarVariations(".");
            if (jarFile != null) return toCrJar(jarFile);
            jarFile = lookForJarVariations("../");
            if (jarFile != null) return toCrJar(jarFile);
        }
        return null;
    }

    public record FileJarPair(
            JarFile jar,
            File file
    ) {}

    public Piece() {
        List<URL> classPath = new ArrayList<>();
        if (getClass().getClassLoader() instanceof URLClassLoader loader) {
            Collections.addAll(classPath, loader.getURLs());
        } else {
            classPath.addAll(ModLocator.getUrlsOnClasspath());
        }

        FileJarPair cosmicReach = searchForCosmicReach();
        if (cosmicReach != null) {
            try {
                classPath.add(cosmicReach.file().toURL());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        ModLocator.crawlModsFolder(classPath);
        classLoader = new PuzzleClassLoader(classPath);

        Thread.currentThread().setContextClassLoader(classLoader);
    }

    public static void main(String[] args) {
        Logger logger = LogManager.getRootLogger();
        Configurator.setAllLevels(logger.getName(), Level.DEBUG);

        new Piece().start(args);
    }

    public void start(String[] args) {

        try {
            final OptionParser parser = new OptionParser();
            parser.allowsUnrecognizedOptions();

            final OptionSet options = parser.parse(args);
            classLoader.addClassLoaderExclusion(providerClass.getName().substring(0, providerClass.getName().lastIndexOf('.')));
            provider = (GameProviderScaffold) Class.forName(providerClass.getName(), true, classLoader).newInstance();

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
